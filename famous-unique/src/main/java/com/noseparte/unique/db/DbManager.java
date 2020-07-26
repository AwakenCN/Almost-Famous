package com.noseparte.unique.db;

import com.noseparte.common.bean.BatchObject;
import com.noseparte.common.bean.BatchObjectEnum;
import com.noseparte.common.exception.UninitializedException;
import com.noseparte.common.global.ConfigManager;
import lombok.extern.slf4j.Slf4j;
import org.iq80.leveldb.*;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.Map;

@Slf4j
public class DbManager {
    private boolean cleanup = false;
    private Charset charset = Charset.forName("utf-8");
    private String defaultDataDir;

    private DB db;
    private WriteOptions writeOptions = new WriteOptions();

    private DbManager() {

    }

    static class DbManagerHolder {
        static DbManager instance = new DbManager();
    }

    public static DbManager getInstance() {
        return DbManagerHolder.instance;
    }

    /**
     * 不指定目录，默认配置文件 leveldb_datadir 这个参数获取
     *
     * @param dataDir 数据文件目录
     */
    public void open(String dataDir) {
        if (dataDir != null && dataDir.trim().length() > 0) {
            this.defaultDataDir = dataDir;
        } else {
            this.defaultDataDir = ConfigManager.getStringValue("unique", "leveldb_datadir");
        }
        DBFactory factory = Iq80DBFactory.factory;
        File file = new File(defaultDataDir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            //如果数据不需要reload，则每次重启，尝试清理磁盘中path下的旧数据。
            if (cleanup) {
                factory.destroy(file, null);//清除文件夹内的所有文件。
            }
            Options options = new Options().createIfMissing(true);
            //重新open新的db
            db = factory.open(file, options);
            log.error("Db open");
        } catch (IOException e) {
            log.error("Db open fail ", e);
        }
    }

    public DB getDb() {
        if (this.db == null) {
            log.error("DB uninitialized ", new UninitializedException());
        }
        return this.db;
    }

    /////select
    public byte[] get(byte[] key) {
        return this.getDb().get(key);
    }

    /////insert
    public Snapshot putAsync(byte[] key, byte[] value) {
        return this.put(key, value, false);
    }

    public Snapshot putSync(byte[] key, byte[] value) {
        return this.put(key, value, true);
    }

    public Snapshot put(byte[] key, byte[] value, boolean sync) {
        return this.getDb().put(key, value, writeOptions.sync(sync));
    }

    //////delete
    public Snapshot deleteAsync(byte[] key) {
        return this.delete(key, false);
    }

    public Snapshot deleteSync(byte[] key) {
        return this.delete(key, true);
    }

    public Snapshot delete(byte[] key, boolean sync) {
        return this.getDb().delete(key, writeOptions.sync(sync));
    }

    //////batch delete or put
    public void batch(LinkedList<BatchObject> batchList) {
        if (batchList == null || batchList.size() == 0) {
            return;
        }
        WriteBatch writeBatch = null;
        try {
            writeBatch = db.createWriteBatch();
            this.batchParse(writeBatch, batchList);
            db.write(writeBatch);
        } finally {
            if (null != writeBatch) {
                try {
                    writeBatch.close();
                } catch (IOException e) {
                    log.error("Db batch operation fail ", e);
                }
            }
        }
    }

    private WriteBatch batchParse(WriteBatch batch, LinkedList<BatchObject> batchList) {
        for (BatchObject batchObject : batchList) {
            BatchObjectEnum batchObjectEnum = batchObject.getBatchObjectEnum();
            switch (batchObjectEnum) {
                case PUT:
                    batch.put(batchObject.getKey(), batchObject.getValue());
                    break;
                case DELETE:
                    batch.delete(batchObject.getKey());
                    break;
                default:
            }
        }
        return batch;
    }

    public void iterator() {
        //iterator，遍历，顺序读
        //读取当前snapshot，快照，读取期间数据的变更，不会反应出来
        Snapshot snapshot = this.getDb().getSnapshot();
        //读选项
        ReadOptions readOptions = new ReadOptions();
        readOptions.fillCache(false);//遍历中swap出来的数据，不应该保存在memtable中。
        readOptions.snapshot(snapshot);
        //默认snapshot为当前。
        DBIterator iterator = db.iterator(readOptions);
        try {
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> item = iterator.next();
                String key = new String(item.getKey(), charset);
                String value = new String(item.getValue(), charset);//null,check.
                log.debug("db___" + key + ":" + value);
            }
        } finally {
            try {
                iterator.close();//must be
            } catch (IOException e) {
                log.error("Db iterator fail ", e);
            }
        }
    }

    public void close() {
        try {
            if (getDb() != null) {
                this.getDb().close();
                log.error("Db close success");
            }
        } catch (IOException e) {
            log.error("Db close fail ", e);
        }
    }
}

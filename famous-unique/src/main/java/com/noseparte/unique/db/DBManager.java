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
import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/8 10:14
 * @Description
 */
@Slf4j
public class DBManager {

    private boolean cleanup = false;
    private Charset charset = Charset.forName("UTF-8");
    private String defaultDataDir;

    private DB db;
    private WriteOptions writeOptions = new WriteOptions();

    public DBManager() {

    }

    static class DBManagerHolder {
        static DBManager instance = new DBManager();
    }

    public static DBManager getInstance() {
        return DBManagerHolder.instance;
    }


    /**
     * 默认读取配置文件中的 levelDb_dataDir
     *
     * @param dataDir
     */
    public void open(String dataDir) {
        if (Objects.nonNull(dataDir) && dataDir.trim().length() > 0) {
            this.defaultDataDir = dataDir;
        } else {
            this.defaultDataDir = ConfigManager.getStringValue("unique", "levelDb_dataDir");
        }
        DBFactory factory = Iq80DBFactory.factory;
        File file = new File(defaultDataDir);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            //如果数据不需要reload，则每次重启， 尝试清理磁盘中path的旧数据。
            if (cleanup) {
                factory.destroy(file, null);
            }
            Options options = new Options().createIfMissing(true);
            //重新open新的db
            db = factory.open(file, options);
            log.debug("DB open");
        } catch (IOException e) {
            log.error("DB open fail ", e);
        }
    }

    public DB getDB() {
        if (this.db == null) {
            log.error("DB uninitialized ", new UninitializedException());
        }
        return this.db;
    }

    /////select
    public byte[] get(byte[] key) {
        return this.getDB().get(key);
    }

    /////insert
    public Snapshot putAsync(byte[] key, byte[] value) {
        return this.put(key, value, false);
    }

    public Snapshot putSync(byte[] key, byte[] value) {
        return this.put(key, value, true);
    }

    public Snapshot put(byte[] key, byte[] value, boolean sync) {
        return this.getDB().put(key, value, writeOptions.sync(sync));
    }

    //////delete
    public Snapshot deleteAsync(byte[] key) {
        return this.delete(key, false);
    }

    public Snapshot deleteSync(byte[] key) {
        return this.delete(key, true);
    }

    public Snapshot delete(byte[] key, boolean sync) {
        return this.getDB().delete(key, writeOptions.sync(sync));
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
        //iterator, 遍历, 顺序读
        //读取当前snapshot, 快照, 读取期间数据的变更，不会同步
        Snapshot snapshot = this.getDB().getSnapshot();
        //读选项
        ReadOptions readOptions = new ReadOptions();
        readOptions.fillCache(false);//遍历中swap出来的数据，不应该保存在MEMTable中
        readOptions.snapshot(snapshot);
        //默认snapshot为当前
        DBIterator iterator = db.iterator(readOptions);
        try {
            while (iterator.hasNext()) {
                Map.Entry<byte[], byte[]> item = iterator.next();
                String key = new String(item.getKey(), charset);
                String value = new String(item.getValue(), charset);
                log.debug("DB__" + key + ":" + value);
            }
        } finally {
            try {
                iterator.close();
            } catch (IOException e) {
                log.error("DB iterator fail ", e);
            }
        }


    }

    public void close() {
        try {
            this.getDB().close();
            log.debug("DB close success");
        } catch (IOException e) {
            log.error("DB close fail ", e);
        }
    }


}

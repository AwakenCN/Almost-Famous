package com.noseparte.unique.service;

import com.noseparte.unique.db.DBManager;
import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.rpc.protocol.RPCUniqueNameService;
import com.noseparte.common.rpc.protocol.UniqueNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

import java.util.Objects;

/**
 * @author Noseparte
 * @date 2019/8/8 12:38
 * @Description
 */
@Slf4j
public class RPCUniqueNameServiceImpl implements RPCUniqueNameService.Iface {
    @Override
    public UniqueNameEnum uniqueName(String name) throws TException {
        if (log.isDebugEnabled()) {
            log.debug("收到需要服务端校验的名称， {}", name);
        }
        byte[] key = (KeyPrefix.UniqueLevelDBPrefix.UNIQUE_NAME + name).getBytes(Misc.CHARSET);
        byte[] value = name.getBytes(Misc.CHARSET);
        byte[] bytes = DBManager.getInstance().get(key);
        if (Objects.nonNull(bytes)) {
            return UniqueNameEnum.REPEAT;
        }
        // save name
        DBManager.getInstance().putSync(key, value);
        return UniqueNameEnum.SUCCESS;
    }
}

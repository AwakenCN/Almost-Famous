package com.noseparte.unique.service;

import com.noseparte.common.rpc.protocol.RPCUniqueIdService;
import com.noseparte.common.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;

/**
 * @author Noseparte
 * @date 2019/8/8 11:28
 * @Description
 */
@Slf4j
public class RPCUniqueIdServiceImpl implements RPCUniqueIdService.Iface {

//    @Autowired
//    private UidGenerator uidGenerator;

    @Override
    public long getUniqueId() throws TException {
        long uid = SnowflakeIdWorker.getUniqueId();
        if (log.isDebugEnabled()) {
            log.debug("uid, {}", uid);
        }
        return uid;
    }

}

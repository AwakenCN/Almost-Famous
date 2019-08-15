package com.liema.unique.service;

import com.liema.common.baidu.UidGenerator;
import com.liema.common.rpc.protocol.RPCUniqueIdService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Noseparte
 * @date 2019/8/8 11:28
 * @Description
 */
@Slf4j
public class RPCUniqueIdServiceImpl implements RPCUniqueIdService.Iface {

    @Autowired
    private UidGenerator uidGenerator;

    @Override
    public long getUniqueId() throws TException {
        long uid = uidGenerator.getUID();
        if (log.isDebugEnabled()) {
            log.debug("uid, {}", uid);
        }
        return uid;
    }

}

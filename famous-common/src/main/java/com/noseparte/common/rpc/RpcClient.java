package com.noseparte.common.rpc;


import com.noseparte.common.rpc.service.UniqueNameEnum;

public interface RpcClient {

    long getUniqueId();

    UniqueNameEnum uniqueName(String name);

    void close();

}

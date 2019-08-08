package com.liema.rpc;

import com.liema.rpc.protocol.UniqueNameEnum;

/**
 * @author Noseparte
 * @date 2019/8/7 15:13
 * @Description
 */
public interface RpcClient {

    void init();

    String getDate(String arg);

    long getUniqueId();

    UniqueNameEnum uniqueName(String name);

    void setHost(String host);

    void setPort(int port);

    void open();

    boolean isOpen();

    void close();

    void flush();
}

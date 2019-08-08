package com.liema.rpc;

import com.liema.rpc.protocol.RPCDateService;
import com.liema.rpc.protocol.UniqueNameEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import com.liema.rpc.protocol.RPCUniqueIdService;
import com.liema.rpc.protocol.RPCUniqueNameService;

/**
 * @author Noseparte
 * @date 2019/8/7 18:05
 * @Description
 */
@Slf4j
public class ThriftClient implements RpcClient {

    private RPCDateService.Client dataService;
    private RPCUniqueIdService.Client uniqueIdService;
    private RPCUniqueNameService.Client uniqueNameService;

    private TBinaryProtocol protocol;
    private TTransport transport;
    private String host;
    private int port;

    @Override
    public void init() {
        transport = new TSocket(host, port);
        protocol = new TBinaryProtocol(transport);
        TMultiplexedProtocol dataProtocol = new TMultiplexedProtocol(protocol, "data");
        TMultiplexedProtocol uniqueNameProtocol = new TMultiplexedProtocol(protocol, "unique_name");
        TMultiplexedProtocol uniqueIdProtocol = new TMultiplexedProtocol(protocol, "unique_id");
        dataService = new RPCDateService.Client(dataProtocol);
        uniqueIdService = new RPCUniqueIdService.Client(uniqueIdProtocol);
        uniqueNameService = new RPCUniqueNameService.Client(uniqueNameProtocol);

        this.open();
    }

    @Override
    public String getDate(String userName) {
        try {
            return this.dataService.getDate(userName);
        } catch (TException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public long getUniqueId() {
        try {
            return this.uniqueIdService.getUniqueId();
        } catch (TException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public UniqueNameEnum uniqueName(String name) {
        try {
            return this.uniqueNameService.uniqueName(name);
        } catch (TException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void open() {
        if (log.isInfoEnabled()) {
            log.info("rpc client open");
        }
        try {
            transport.open();
        } catch (TTransportException e) {
            log.error("rpc open fail ", e);
        }
    }

    @Override
    public synchronized boolean isOpen() {
        return transport.isOpen();
    }

    @Override
    public void close() {
        if (log.isInfoEnabled()) {
            log.info("rpc client close");
        }
        transport.close();
    }

    @Override
    public void flush() {
        if (log.isInfoEnabled()) {
            log.info("rpc client flush");
        }
        try {
            transport.flush();
        } catch (TTransportException e) {
            log.error("rpc client flush fail", e);
        }
    }
}

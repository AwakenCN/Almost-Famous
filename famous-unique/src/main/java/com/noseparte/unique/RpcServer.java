package com.noseparte.unique;

import com.noseparte.common.global.ConfigManager;
import com.noseparte.common.global.ExitHandler;
import com.noseparte.common.rpc.protocol.RPCDateService;
import com.noseparte.common.rpc.protocol.RPCUniqueIdService;
import com.noseparte.common.rpc.protocol.RPCUniqueNameService;
import com.noseparte.unique.db.DBManager;
import com.noseparte.unique.service.RPCDataServiceImpl;
import com.noseparte.unique.service.RPCUniqueIdServiceImpl;
import com.noseparte.unique.service.RPCUniqueNameServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportFactory;

/**
 * @author Noseparte
 * @date 2019/8/5 16:48
 * @Description
 */
@Slf4j
public class RpcServer {

    private int port;
    private int minThreads;
    private int maxThreads;

    private TBinaryProtocol.Factory protocolFactory;
    private TTransportFactory transportFactory;

    private RPCDateService.Iface rpcDataService = new RPCDataServiceImpl();
    private RPCUniqueIdService.Iface rpcUniqueIdService = new RPCUniqueIdServiceImpl();
    private RPCUniqueNameService.Iface rpcUniqueNameService = new RPCUniqueNameServiceImpl();

    public void init() {
        port = ConfigManager.getIntegerValue("unique", "thrift_port");
        minThreads = ConfigManager.getIntegerValue("unique", "thrift_min_threads");
        maxThreads = ConfigManager.getIntegerValue("unique", "thrift_max_threads");
        protocolFactory = new TBinaryProtocol.Factory();
        transportFactory = new TTransportFactory();
    }

    public void start() {
        RPCDateService.Processor dateProcessor = new RPCDateService.Processor<>(rpcDataService);
        RPCUniqueNameService.Processor uniqueNameProcessor = new RPCUniqueNameService.Processor<>(rpcUniqueNameService);
        RPCUniqueIdService.Processor uniqueIdProcessor = new RPCUniqueIdService.Processor<>(rpcUniqueIdService);

        TMultiplexedProcessor processor = new TMultiplexedProcessor();
        processor.registerProcessor("date", dateProcessor);
        processor.registerProcessor("unique_id", uniqueIdProcessor);
        processor.registerProcessor("unique_name", uniqueNameProcessor);
        ////////////////////////
        init();
        ////////////////////////
        try {
            TServerTransport transport = new TServerSocket(port);
            TThreadPoolServer.Args tArgs = new TThreadPoolServer.Args(transport).processor(processor)
                    .protocolFactory(protocolFactory)
                    .transportFactory(transportFactory)
                    .minWorkerThreads(minThreads)
                    .maxWorkerThreads(maxThreads);
            TServer server = new TThreadPoolServer(tArgs);
            log.info("thrift服务启动成功， 端口={}", port);
            log.info("最小线程数={}， 最大线程数={}", minThreads, maxThreads);
            server.serve();
        } catch (Exception e) {
            log.error("thrift服务启动失败， {}", e);
        }
    }

    public static void shutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                DBManager.getInstance().close();
            } catch (Exception e) {
                log.error("Jvm shutDownHook ", e);
            }
        }));
    }

    public static void main(String[] args) {

        String path = RpcServer.class.getResource("/properties").getFile();
        ConfigManager.init(path);

        RpcServer server = new RpcServer();
        ExitHandler exitHandler = new ExitHandler();
        exitHandler.registerSignal("TERM");

        // exit hook
        shutDownHook();
        // levelDB
        DBManager.getInstance().open("");
        // levelDB browse data
        DBManager.getInstance().iterator();
        // thrift
        server.start();
    }
}

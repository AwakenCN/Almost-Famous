package com.noseparte.unique;

import com.noseparte.common.global.ConfigManager;
import com.noseparte.unique.db.DbManager;
import com.noseparte.unique.service.RPCUniqueIdServiceImpl;
import com.noseparte.unique.service.RPCUniqueNameServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Noseparte
 * @date 2019/8/5 16:48
 * @Description
 */
@Slf4j
public class RpcServer {

    public final static Logger LOG = LoggerFactory.getLogger("Unique");

    private Server server;

    private int port = ConfigManager.getIntegerValue("unique", "gRpc_port");

    private void start() throws IOException {
        server = ServerBuilder.forPort(port)
                .addService(new RPCUniqueIdServiceImpl())
                .addService(new RPCUniqueNameServiceImpl())
                .build()
                .start();
        LOG.info("Server started, listening on " + port);
    }

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public void shutDownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                LOG.debug("*** shutting down gRPC com.liema.match.server since JVM is shutting down");
                // close db
                LOG.debug("*** close db");
                DbManager.getInstance().close();
                LOG.debug("*** com.liema.match.server shut down");
                stop();
            } catch (Exception e) {
                LOG.error("Jvm shutDownHook ", e);
            }
        }));
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String configPath;
        if (args.length > 0) {
            configPath = args[0] + "/properties";
        } else {
            configPath = RpcServer.class.getResource("/properties").getFile();
        }
        ConfigManager.init(configPath);

        RpcServer uniqueServer = new RpcServer();
        // exit hook
        uniqueServer.shutDownHook();
        // leveldb
        DbManager.getInstance().open("");
        // leveldb browse data
        DbManager.getInstance().iterator();
        // grpc
        uniqueServer.start();
        uniqueServer.blockUntilShutdown();
    }
}

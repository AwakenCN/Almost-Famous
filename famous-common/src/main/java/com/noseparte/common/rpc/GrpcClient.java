package com.noseparte.common.rpc;

import com.noseparte.common.rpc.service.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class GrpcClient implements RpcClient {

    private static final Logger LOG = LogManager.getLogger(GrpcClient.class);

    private final ManagedChannel channel;
    private final UniqueIdServiceGrpc.UniqueIdServiceBlockingStub uniqueIdServiceBlockingStub;
    private final UniqueNameServiceGrpc.UniqueNameServiceBlockingStub uniqueNameServiceBlockingStub;

    public GrpcClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build());
    }

    public GrpcClient(ManagedChannel channel) {
        this.channel = channel;
        uniqueIdServiceBlockingStub = UniqueIdServiceGrpc.newBlockingStub(channel);
        uniqueNameServiceBlockingStub = UniqueNameServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public long getUniqueId() {
        try {
            UniqueIdRequest request = UniqueIdRequest.newBuilder().build();
            UniqueIdResponse response = uniqueIdServiceBlockingStub.getUniqueId(request);
            if (null != response)
                return response.getUniqueId();
        } catch (StatusRuntimeException e) {
            LOG.error("RPC get unique id error: {}", e);
        }
        return 0;
    }

    @Override
    public UniqueNameEnum uniqueName(String name) {
        try {
            if (StringUtils.isEmpty(name)) {
                LOG.error("Check name is empty.");
                return UniqueNameEnum.FAIL;
            }
            UniqueNameRequest request = UniqueNameRequest.newBuilder().setName(name).build();
            UniqueNameResponse response = uniqueNameServiceBlockingStub.uniqueName(request);
            if (null != response)
                return response.getState();
        } catch (StatusRuntimeException e) {
            LOG.error("RPC get unique name error: {}", e);
        }
        return UniqueNameEnum.FAIL;
    }

    @Override
    public void close() {
        try {
            if (channel != null && !channel.isTerminated()) {
                LOG.info("Rpc client close.");
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            LOG.error("Rpc client close error:", e);
        }
    }

}

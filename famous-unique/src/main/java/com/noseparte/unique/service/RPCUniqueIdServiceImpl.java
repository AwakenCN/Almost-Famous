package com.noseparte.unique.service;

import com.noseparte.common.rpc.service.UniqueIdRequest;
import com.noseparte.common.rpc.service.UniqueIdResponse;
import com.noseparte.common.rpc.service.UniqueIdServiceGrpc;
import com.noseparte.common.utils.SnowflakeIdWorker;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RPCUniqueIdServiceImpl extends UniqueIdServiceGrpc.UniqueIdServiceImplBase {

    protected static Logger LOG = LoggerFactory.getLogger("Unique");

    @Override
    public void getUniqueId(UniqueIdRequest request, StreamObserver<UniqueIdResponse> responseObserver) {
        long id = SnowflakeIdWorker.getUniqueId();
        if (LOG.isDebugEnabled()) {
            LOG.debug("获得的唯一id={}", id);
        }
        UniqueIdResponse build = UniqueIdResponse.newBuilder().setUniqueId(id).build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }
}

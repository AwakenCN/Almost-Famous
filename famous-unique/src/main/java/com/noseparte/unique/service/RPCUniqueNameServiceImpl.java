package com.noseparte.unique.service;

import com.noseparte.common.global.KeyPrefix;
import com.noseparte.common.global.Misc;
import com.noseparte.common.rpc.service.UniqueNameEnum;
import com.noseparte.common.rpc.service.UniqueNameRequest;
import com.noseparte.common.rpc.service.UniqueNameResponse;
import com.noseparte.common.rpc.service.UniqueNameServiceGrpc;
import com.noseparte.unique.db.DbManager;
import io.grpc.stub.StreamObserver;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPCUniqueNameServiceImpl extends UniqueNameServiceGrpc.UniqueNameServiceImplBase {

    protected static Logger LOG = LoggerFactory.getLogger("Unique");

    @Override
    public void uniqueName(UniqueNameRequest request, StreamObserver<UniqueNameResponse> responseObserver) {
        String name = request.getName();
        if (LOG.isDebugEnabled()) {
            LOG.debug("收到校验唯一名{}请求", name);
        }

        if (StringUtils.isEmpty(name)) {
            response(responseObserver, UniqueNameEnum.FAIL);
            return;
        }

        byte[] key = (KeyPrefix.UniqueLevelDBPrefix.UNIQUE_NAME + name).getBytes(Misc.CHARSET);
        byte[] value = name.getBytes(Misc.CHARSET);
        byte[] bytes = DbManager.getInstance().get(key);
        // check unique name
        if (bytes != null) {
            response(responseObserver, UniqueNameEnum.REPEAT);
            return;
        }
        // save name
        DbManager.getInstance().putSync(key, value);
        response(responseObserver, UniqueNameEnum.SUCCESS);
    }

    private void response(StreamObserver<UniqueNameResponse> responseObserver, UniqueNameEnum state) {
        UniqueNameResponse build = UniqueNameResponse.newBuilder().setState(state).build();
        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }
}

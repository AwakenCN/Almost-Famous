package com.noseparte.match.asynchttp;

import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import org.apache.http.HttpResponse;

public class ConsulServicesRequest extends RequestAsync {
    @Override
    public void execute() throws Exception {

    }

    class ConsulServicesResponse implements ResponseCallBack<HttpResponse> {

        @Override
        public void completed(HttpResponse result) {

        }

        @Override
        public void failed(Exception ex) {

        }

        @Override
        public void cancelled() {

        }
    }

}

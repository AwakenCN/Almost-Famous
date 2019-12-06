package com.noseparte.match.asynchttp;

import com.noseparte.common.http.RequestAsync;
import com.noseparte.common.http.ResponseCallBack;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;

@Slf4j
public class RegistryConsulRequest extends RequestAsync {
    @Override
    public void execute() throws Exception {

    }

    class RegistryConsulResponse implements ResponseCallBack<HttpResponse> {

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

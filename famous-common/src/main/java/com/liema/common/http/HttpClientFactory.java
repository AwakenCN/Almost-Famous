package com.liema.common.http;


public class HttpClientFactory {

    private static HttpAsyncClient httpAsyncClient = new HttpAsyncClient();

    private HttpClientFactory() {
    }

    private static HttpClientFactory httpClientFactory = new HttpClientFactory();

    public static HttpClientFactory getInstance() {
        return httpClientFactory;
    }

    public HttpAsyncClient getHttpAsyncClientPool() {
        return httpAsyncClient;
    }

}

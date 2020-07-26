package com.noseparte.common.http;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.utils.FastJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@Slf4j
public abstract class RequestSync {

    public abstract void execute() throws Exception;

    public abstract JSONObject callback() throws Exception;

    protected void sync(String baseUrl, boolean isPost,
                              List<KeyValuePair> urlParams,
                              List<KeyValuePair> postBody,
                              List<KeyValuePair> headers,
                              ResponseCallBack<HttpResponse> callback)
            throws Exception {


        if (baseUrl == null) {
            log.error("we don't have utils url, check config");
            throw new Exception("missing utils url");
        }

        HttpRequestBase httpMethod;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClientFactory.getInstance().getHttpSyncClientPool().getSyncHttpClient();
            HttpClientContext localContext = HttpClientContext.create();
            BasicCookieStore cookieStore = new BasicCookieStore();

            if (isPost) {
                httpMethod = new HttpPost(baseUrl);
                if (null != postBody) {
                    if (log.isDebugEnabled()) {
                        log.debug("async post postBody = {}", postBody);
                    }
                    JSONObject jsonObject = new JSONObject();
                    for (KeyValuePair keyValuePair : postBody) {
                        jsonObject.put(keyValuePair.name, keyValuePair.value);
                    }
                    StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    ((HttpPost) httpMethod).setEntity(entity);
                }
            } else {
                httpMethod = new HttpGet(baseUrl);
                if (null != urlParams) {
                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));
                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }
            }

            if (null != headers) {
                for (KeyValuePair keyValuePair : headers) {
                    httpMethod.addHeader(keyValuePair.getName(), keyValuePair.getValue());
                }
            }

            if (log.isDebugEnabled()) {
                log.debug("async get params: " + httpMethod.getURI());
            }
            localContext.setAttribute(HttpClientContext.COOKIE_STORE,
                    cookieStore);
            try (CloseableHttpResponse response = httpClient.execute(httpMethod, localContext)) {
                callback.completed(response);
            }
        } catch (Exception e) {
            log.error("RequestSync send error: ", e);
        }
    }

    protected JSONObject syncCallBack(String baseUrl, boolean isPost,
                                      List<KeyValuePair> urlParams,
                                      List<KeyValuePair> postBody,
                                      List<KeyValuePair> headers)
            throws Exception {


        if (baseUrl == null) {
            log.error("we don't have utils url, check config");
            throw new Exception("missing utils url");
        }

        HttpRequestBase httpMethod;
        CloseableHttpClient httpClient = null;
        try {
            httpClient = HttpClientFactory.getInstance().getHttpSyncClientPool().getSyncHttpClient();
            HttpClientContext localContext = HttpClientContext.create();
            BasicCookieStore cookieStore = new BasicCookieStore();

            if (isPost) {
                httpMethod = new HttpPost(baseUrl);
                if (null != postBody) {
                    if (log.isDebugEnabled()) {
                        log.debug("async post postBody = {}", postBody);
                    }
                    JSONObject jsonObject = new JSONObject();
                    for (KeyValuePair keyValuePair : postBody) {
                        jsonObject.put(keyValuePair.name, keyValuePair.value);
                    }
                    StringEntity entity = new StringEntity(jsonObject.toString(), "UTF-8");
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    ((HttpPost) httpMethod).setEntity(entity);
                }
            } else {
                httpMethod = new HttpGet(baseUrl);
                if (null != urlParams) {
                    String getUrl = EntityUtils
                            .toString(new UrlEncodedFormEntity(urlParams));
                    httpMethod.setURI(new URI(httpMethod.getURI().toString()
                            + "?" + getUrl));
                }
            }

            if (null != headers) {
                for (KeyValuePair keyValuePair : headers) {
                    httpMethod.addHeader(keyValuePair.getName(), keyValuePair.getValue());
                }
            }

//            if (log.isDebugEnabled()) {
//                log.debug("async get params: " + httpMethod.getURI());
//            }
            localContext.setAttribute(HttpClientContext.COOKIE_STORE,
                    cookieStore);
            CloseableHttpResponse response = httpClient.execute(httpMethod, localContext);
            return getJSONObject(response);
        } catch (Exception e) {
            log.error("RequestSync send error: ", e);
        }
        return null;
    }

    protected void sync(String baseUrl, List<KeyValuePair> postBody, List<KeyValuePair> headers, ResponseCallBack<HttpResponse> callback)
            throws Exception {
        this.sync(baseUrl, true, null, postBody, headers, callback);
    }

    protected void sync(String baseUrl, List<KeyValuePair> postBody, ResponseCallBack<HttpResponse> callback)
            throws Exception {
        this.sync(baseUrl, true, null, postBody, null, callback);
    }

    protected JSONObject syncCallBack(String baseUrl, List<KeyValuePair> postBody)
            throws Exception {
        return this.syncCallBack(baseUrl, true, null, postBody, null);
    }

    protected String getHttpContent(HttpResponse response) {

        HttpEntity entity = response.getEntity();
        String body = null;

        if (entity == null) {
            return null;
        }

        try {
            body = EntityUtils.toString(entity, "utf-8");
        } catch (ParseException | IOException e) {
            log.error("the response's content input stream is corrupt", e);
        }
        return body;
    }

    protected JSONObject getJSONObject(HttpResponse result) {

        String response = getHttpContent(result);

//        if (log.isDebugEnabled()) {
//            log.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
//        }

        return FastJsonUtils.parseObject(response);
    }


}

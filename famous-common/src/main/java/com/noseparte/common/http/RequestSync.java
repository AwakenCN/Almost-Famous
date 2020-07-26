package com.noseparte.common.http;

import com.alibaba.fastjson.JSONObject;
import com.noseparte.common.utils.FastJsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;

public abstract class RequestSync {

    private static Logger LOG = LoggerFactory
            .getLogger(RequestSync.class);

    public abstract void execute() throws Exception;

    protected void sync(String baseUrl, boolean isPost,
                        List<KeyValuePair> urlParams,
                        List<KeyValuePair> postBody,
                        List<KeyValuePair> headers,
                        FutureCallback callback)
            throws Exception {


        if (baseUrl == null) {
            LOG.error("we don't have base url, check config");
            throw new Exception("missing base url");
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
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("async post postBody = {}", postBody);
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

            if (LOG.isDebugEnabled()) {
                LOG.debug("async get params: " + httpMethod.getURI());
            }
            localContext.setAttribute(HttpClientContext.COOKIE_STORE,
                    cookieStore);
            CloseableHttpResponse response = httpClient.execute(httpMethod, localContext);
            callback.completed(response);
        } catch (Exception e) {
            LOG.error("RequestSync send error: ", e);
        }
    }

    protected void sync(String baseUrl, List<KeyValuePair> postBody, List<KeyValuePair> headers, FutureCallback callback)
            throws Exception {
        this.sync(baseUrl, true, null, postBody, headers, callback);
    }

    protected void sync(String baseUrl, List<KeyValuePair> postBody, FutureCallback callback)
            throws Exception {
        this.sync(baseUrl, true, null, postBody, null, callback);
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
            LOG.error("the response's content input stream is corrupt", e);
        }
        return body;
    }

    protected JSONObject getJSONObject(HttpResponse result) {

        String response = getHttpContent(result);
        if (LOG.isDebugEnabled()) {
            LOG.debug("返回状态码{}, 返回内容{}", result.getStatusLine().getStatusCode(), response);
        }

        return FastJsonUtils.parseObject(response);
    }


}

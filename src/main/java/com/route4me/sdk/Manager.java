package com.route4me.sdk;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.route4me.sdk.exception.APIException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

@RequiredArgsConstructor
public abstract class Manager {

    private final String apiKey;
    protected final Gson gson;

    protected static URIBuilder defaultBuilder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setHost(Route4Me.HOST);
        builder.setPath(endpoint);
        return builder;
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, List<NameValuePair> body, Class<T> clazz) throws APIException {
        try {
            return this.makeRequest(method, builder, new UrlEncodedFormEntity(body), clazz, null);
        } catch (UnsupportedEncodingException ex) {
            throw new APIException(ex);
        }
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Class<T> clazz) throws APIException {
        try {
            return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body) : (HttpEntity) null, clazz, null);
        } catch (UnsupportedEncodingException ex) {
            throw new APIException(ex);
        }
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz) throws APIException {
        return this.makeRequest(method, builder, body, clazz, null);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Type type) throws APIException {
        return this.makeRequest(method, builder, body, null, type);
    }
    
    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type) throws APIException {
        try {
            builder.addParameter("api_key", this.apiKey);
            HttpRequestBase hrb = method.create(builder.build());
            //if there's any entity to be attached to the body and request supports it, do it.
            if (body != null && hrb instanceof HttpEntityEnclosingRequestBase) {
                ((HttpEntityEnclosingRequestBase) hrb).setEntity(body);
            } else if (body != null) {
                throw new RuntimeException("Method does not support body!");
            }
            //try with resources to close streams
            try (CloseableHttpResponse resp = HttpClients.createDefault().execute(hrb);
                    InputStream is = resp.getEntity().getContent()) {
                if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() >= 300) {
                    throw new APIException(String.format("Invalid status code %d", resp.getStatusLine().getStatusCode()));
                }
                if (clazz == null && type == null) {
                    return null;
                }
                //response should always be present
                if (is == null) {
                    throw new APIException("Response body is null.");
                }
                try (InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr)) {
                    //deserialize json into an object
                    if (clazz != null) {
                        return this.gson.fromJson(br, clazz);
                    }
                    return this.gson.fromJson(br, type);
                }
            } catch (IOException ex) {
                throw new APIException(ex);
            }
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}

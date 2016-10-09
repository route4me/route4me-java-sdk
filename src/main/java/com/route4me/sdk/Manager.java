package com.route4me.sdk;

import com.google.gson.Gson;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.responses.ErrorResponse;
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

import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;

@RequiredArgsConstructor
public abstract class Manager {

    private final String apiKey;
    protected final Gson gson;

    protected static URIBuilder defaultBuilder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("www.route4me.com");
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

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Type type) throws APIException {
        try {
            return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body) : (HttpEntity) null, type);
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

    protected String makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body) throws APIException {
        return this.makeRequest(method, builder, body, String.class, null);
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
                //response should always be present
                if (is == null) {
                    throw new APIException("Response body is null.");
                }
                try (InputStreamReader isr = new InputStreamReader(is);
                     BufferedReader br = new BufferedReader(isr)) {
                    if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() >= 300) {
                        try {
                            ErrorResponse er = this.gson.fromJson(br, ErrorResponse.class);
                            throw new APIException(String.format("Invalid status code %d, errors: %s", resp.getStatusLine().getStatusCode(), er.getErrors().toString()));
                        } catch (Exception e) {
                            throw new APIException(String.format("Invalid status code %d and no error present.", resp.getStatusLine().getStatusCode()));
                        }
                    }
                    //deserialize json into an object
                    if (clazz != null && clazz != String.class) {
                        return this.gson.fromJson(br, clazz);
                    } else if (type != null) {
                        return this.gson.fromJson(br, type);
                    }
                    if (clazz == String.class) {
                        StringBuilder respString = new StringBuilder();
                        String line = "";
                        while ((line = br.readLine()) != null) {
                            respString.append(line);
                        }
                        return (T) respString.toString();
                    }
                    return null;
                }
            } catch (IOException ex) {
                throw new APIException(ex);
            }
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}

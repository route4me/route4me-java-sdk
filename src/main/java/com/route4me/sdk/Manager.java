package com.route4me.sdk;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

public abstract class Manager {

    private final String apiKey;
    protected Gson gson;
    private RequestConfig requestProxyConfig;
    private boolean disableRedirects;

    public Manager(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public Manager(String apiKey, boolean disableRedirects) {
        this(apiKey);
        this.disableRedirects = disableRedirects;
    }

    public Manager(String apiKey, GsonBuilder builder) {
        this.apiKey = apiKey;
        this.gson = builder.addSerializationExclusionStrategy(new ExclusionStrategy() {
            @Override
            public boolean shouldSkipField(FieldAttributes fieldAttributes) {
                //skip fields without SerializedName
                return fieldAttributes.getAnnotation(SerializedName.class) == null;
            }

            @Override
            public boolean shouldSkipClass(Class<?> aClass) {
                return false;
            }
        }).create();
    }

    public Manager(String apiKey, String proxyHost, int proxyPort, String proxySchema) {
        this(apiKey);
        HttpHost proxy = new HttpHost(proxyHost, proxyPort, proxySchema);
        this.requestProxyConfig = RequestConfig.custom()
                .setProxy(proxy)
                .build();
    }

    public Manager(String apiKey, String proxyHost, int proxyPort, String proxySchema, boolean disableRedirects) {
        this(apiKey, proxyHost, proxyPort, proxySchema);
        this.disableRedirects = disableRedirects;
    }

    protected static URIBuilder defaultBuilder(String endpoint) {
        URIBuilder builder = new URIBuilder();
        builder.setScheme("https");
        builder.setHost("api.route4me.com");
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
        return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body, "UTF-8") : (HttpEntity) null, clazz, null);
    }

    protected <T> T makeRequest(RequestMethod method, URIBuilder builder, String body, Type type) throws APIException {
        return this.makeRequest(method, builder, !body.isEmpty() ? new StringEntity(body, "UTF-8") : (HttpEntity) null, type);
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

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body,  Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = QueryConverter.convertObjectToParameters(requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(body), "UTF-8"), clazz, null, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams, Type type) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = QueryConverter.convertObjectToParameters(requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Manager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(body), "UTF-8"), null, type, "application/json");
    }
    
    
    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = QueryConverter.convertObjectToParameters(requestParams);
                if (method == RequestMethod.GET){
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.toString() + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(requestParams), "UTF-8"), clazz, null, "application/json");
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, clazz, null, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Type type) throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = QueryConverter.convertObjectToParameters(requestParams);
                if (method == RequestMethod.GET){
                    builder.addParameters(params);
                }
            } catch (IllegalAccessException e) {
                throw new APIException("Could not convert " + requestParams.toString() + " to query parameters.", e);
            }
            if (method != RequestMethod.GET) {
                return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(requestParams), "UTF-8"), null, type, "application/json");
            }
        }
        return this.makeRequest(method, builder, (HttpEntity) null, null, type, "application/json");
    }

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type) throws APIException{
        return this.makeRequest(method, builder, body, clazz, type, null);
    }
    
    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type, String requestContentType ) throws APIException {
        try {
            // Redirects Management
            CloseableHttpClient client = HttpClients.createDefault();
            if (this.disableRedirects) {
                builder.addParameter("redirect", "0");
                client = HttpClients.custom().disableRedirectHandling().build();
            }
            builder.addParameter("api_key", this.apiKey);
            HttpRequestBase hrb = method.create(builder.build());
            if (requestContentType != null){
                hrb.setHeader("Content-type", requestContentType);
            }
            // Checking If a proxy was set. 
            if (this.requestProxyConfig != null) {
                hrb.setConfig(this.requestProxyConfig);
            }
            //if there's any entity to be attached to the body and request supports it, do it.
            if (body != null && hrb instanceof HttpEntityEnclosingRequestBase) {
                ((HttpEntityEnclosingRequestBase) hrb).setEntity(body);
            } else if (body != null) {
                throw new RuntimeException("Method does not support body!");
            }
            //try with resources to close streams
            try (CloseableHttpResponse resp = client.execute(hrb);
                    InputStream is = resp.getEntity().getContent()) {
                //response should always be present
                if (is == null) {
                    throw new APIException("Response body is null.");
                }
                try (InputStreamReader isr = new InputStreamReader(is);
                        BufferedReader br = new BufferedReader(isr)) {
                    if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() > 303) {
                        try {
                            StringBuilder respString = new StringBuilder();
                            String line = "";
                            while ((line = br.readLine()) != null) {
                                respString.append(line);
                            }
                            throw new APIException(String.format("Invalid status code %d, errors: %s", resp.getStatusLine().getStatusCode(), respString.toString()));
                        } catch (APIException | IOException e) {
                            System.err.println(e);
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

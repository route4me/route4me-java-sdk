package com.route4me.sdk;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;
import com.route4me.sdk.exception.APIException;
import com.route4me.sdk.queryconverter.QueryConverter;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;

public abstract class Manager {

    protected static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(Manager.class);
    private final String apiKey;
    protected Gson gson;
    private RequestConfig requestProxyConfig;
    private boolean disableRedirects;
    private String callBackURL;
    

    public Manager(String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
    }

    public Manager(String apiKey, boolean disableRedirects) {
        this(apiKey);
        this.disableRedirects = disableRedirects;
    }

    public Manager(String apiKey, boolean disableRedirects, String callBackURL) {
        this(apiKey);
        this.disableRedirects = disableRedirects;
        this.callBackURL = callBackURL;
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

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object body, Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            List<NameValuePair> params;
            try {
                params = QueryConverter.convertObjectToParameters(requestParams);
                builder.addParameters(params);
            } catch (IllegalAccessException ex) {
                logger.error(ex);
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
                logger.error(ex);
            }
        }
        return this.makeRequest(method, builder, new StringEntity(this.gson.toJson(body), "UTF-8"), null, type, "application/json");
    }

    protected <T> T makeJSONRequest(RequestMethod method, URIBuilder builder, Object requestParams, Class<T> clazz) throws APIException {
        if (requestParams != null) {
            try {
                List<NameValuePair> params = QueryConverter.convertObjectToParameters(requestParams);
                if (method == RequestMethod.GET) {
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
                if (method == RequestMethod.GET) {
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

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type) throws APIException {
        return this.makeRequest(method, builder, body, clazz, type, null);
    }

    private <T> T makeRequest(RequestMethod method, URIBuilder builder, HttpEntity body, Class<T> clazz, Type type, String requestContentType) throws APIException {
        try {
            // Redirects Management
            CloseableHttpClient client = HttpClients.createDefault();
            if (this.disableRedirects) {
                builder.addParameter("redirect", "0");
                client = HttpClients.custom().disableRedirectHandling().build();
            }
            if (this.callBackURL != null) {
                builder.addParameter("optimized_callback_url", this.callBackURL);
            }
            if (this.apiKey != null) {
                builder.addParameter("api_key", this.apiKey);
            }
            HttpRequestBase hrb = method.create(builder.build());
            if (requestContentType != null) {
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
            try (CloseableHttpResponse resp = client.execute(hrb); InputStream is = resp.getEntity().getContent()) {
                //response should always be present
                if (is == null) {
                    throw new APIException("Response body is null.");
                }
                try (InputStreamReader isr = new InputStreamReader(is); BufferedReader br = new BufferedReader(isr)) {
                    if (resp.getStatusLine().getStatusCode() < 200 || resp.getStatusLine().getStatusCode() > 303) {
                        try {
                            StringBuilder respString = responseContentParser(br);
                            throw new APIException(String.format("Invalid status code %d, errors: %s", resp.getStatusLine().getStatusCode(), respString.toString()));
                        } catch (IOException e) {
                            throw new APIException(String.format("Invalid status code %d and no error present.", resp.getStatusLine().getStatusCode()));
                        }
                    }
                    //deserialize json into an object
                    if (clazz != null && clazz != String.class) {
                        StringBuilder respString = responseContentParser(br);
                        try {
                            return this.gson.fromJson(respString.toString(), clazz);
                        } catch (JsonSyntaxException ex) {
                            throw new APIException(ex.toString());
                        }
                    } else if (type != null) {
                        StringBuilder respString = responseContentParser(br);
                        try {
                            return this.gson.fromJson(respString.toString(), type);
                        } catch (IllegalStateException ex) {
                            logger.error(ex);
                            throw new APIException(String.format("Error parsing JSON response from server. Illegal state error: %s", respString.toString()));
                        } catch (JsonSyntaxException ex) {
                            logger.error(String.format("Error parsing JSON response from server. Unexpected syntax: %s", respString.toString()));
                            throw new APIException(String.format("Unexpected Server response: %s", respString.toString()));
                        }
                    }
                    if (clazz == String.class) {
                        StringBuilder respString = responseContentParser(br);
                        return (T) respString.toString();
                    }
                    return null;
                }
            } catch (IOException ex) {
                throw new APIException(ex);
            } catch (JsonSyntaxException ex) {
                logger.error(ex);
                return null;
            }
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private StringBuilder responseContentParser(BufferedReader br) throws IOException {
        StringBuilder respString = new StringBuilder();
        String line = "";
        while ((line = br.readLine()) != null) {
            respString.append(line);
        }
        return respString;
    }

    /**
     * @return the callBackURL
     */
    public String getCallBackURL() {
        return callBackURL;
    }

    /**
     * @param callBackURL the callBackURL to set
     */
    public void setCallBackURL(String callBackURL) {
        this.callBackURL = callBackURL;
    }
    
}

package com.route4me.sdk.managers;

import com.route4me.sdk.http.HttpDeleteWithBody;
import com.google.gson.Gson;
import com.route4me.sdk.serdes.DataObjectSerializer;
import com.route4me.sdk.Route4Me;
import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.enums.APIEndPoints;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 *
 * @author juan
 */
public abstract class Manager {

    private Response response;

    private Gson gson = new Gson();

    public static String getResponseBody(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String output = "";
        StringBuffer response = new StringBuffer();
        try {
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toString();
    }

    private static String urlEncodePair(String k, String v) throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, Route4Me.CHARSET);
        }
    }

    public static Response makeURLConnectionRequest(RequestMethod method, String url, String params) throws APIConnectionException, IOException {
        return makeURLConnectionRequest(method, url, params, (String) null);
    }

    /**
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    /**
     * @return the gson
     */
    public Gson getGson() {
        return gson;
    }

    /**
     * @param gson the gson to set
     */
    public void setGson(Gson gson) {
        this.gson = gson;
    }

    protected enum RequestMethod {

        GET, POST, POST_FORM, DELETE, PUT, DELETE_BODY;
    }

    protected static Response makeURLConnectionRequest(RequestMethod method, String url, String params, String data) throws APIConnectionException, IOException {
        return makeURLConnectionRequest(method, url, params, data, null);
    }

    protected static Response makeURLConnectionRequest(RequestMethod method, String url, String params, List<NameValuePair> data) throws APIConnectionException, IOException {
        return makeURLConnectionRequest(method, url, params, null, data);
    }


    protected static Response makeURLConnectionRequest(RequestMethod method, String url, String params, String data, List<NameValuePair> listData) throws APIConnectionException, IOException {
        CloseableHttpResponse response = null;
        try {
            switch (method) {
                case GET:
                    response = createGetConnection(url, params);
                    break;
                case PUT:
                    response = createPutConnection(url, params, data);
                    break;
                case POST:
                    response = createPostConnection(url, params, data);
                    break;
                case POST_FORM:
                    response = createPostFormConnection(url, params, listData);
                    break;
                case DELETE:
                    response = createDeleteConnection(url, params);
                    break;
                case DELETE_BODY:
                    response = createDeleteConnection(url, params, data);
                    break;
                default:
                    throw new APIConnectionException(String.format("Unrecognized HTTP method %s. ", method));
            }
            HttpEntity entity = response.getEntity();
            // trigger the request
            String responseBody = null;
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers;
            if (statusCode >= 200 && statusCode < 300) {
                responseBody = Manager.getResponseBody(entity.getContent());
            } else if (entity.getContent() != null) {
                responseBody = Manager.getResponseBody(entity.getContent());
            }
            headers = response.getAllHeaders();
            String responseMessage = "";
            return new Response(statusCode, responseBody, responseMessage, headers);
        } catch (IOException e) {
            throw new APIConnectionException(String.format("Could not connect to Route4Me (%s). ", APIEndPoints.MAIN_HOST), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    protected static CloseableHttpResponse createPostConnection(String url, String params, String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(formatURL(url, params));
        post.setEntity(new StringEntity(data));
        CloseableHttpResponse response = null;
        response = httpClient.execute(post);
        return response;
    }

    protected static CloseableHttpResponse createPostFormConnection(String url, String params, List <NameValuePair> data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(formatURL(url, params));
        post.setEntity(new UrlEncodedFormEntity(data));
        CloseableHttpResponse response = null;
        response = httpClient.execute(post);
        return response;
    }

    protected static CloseableHttpResponse createPutConnection(String url, String params, String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut put = new HttpPut(formatURL(url, params));
        put.setEntity(new StringEntity(data));
        CloseableHttpResponse response = null;
        response = httpClient.execute(put);
        return response;
    }

    protected static String formatURL(String url, String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            // In some cases, URL can already contain a question mark (eg, upcoming invoice lines)
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    protected static String transformParams(Map<String, String> params) throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(), entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    protected static CloseableHttpResponse createDeleteConnection(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(formatURL(url, params));
        CloseableHttpResponse response = null;
        response = httpClient.execute(delete);
        return response;
    }

    protected static CloseableHttpResponse createDeleteConnection(String url, String params, String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDeleteWithBody delete = new HttpDeleteWithBody(formatURL(url, params));
        delete.setEntity(new StringEntity(data));
        CloseableHttpResponse response = null;
        response = httpClient.execute(delete);
        return response;
    }

    protected static CloseableHttpResponse createGetConnection(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(formatURL(url, params));
        CloseableHttpResponse response = null;
        response = httpClient.execute(get);
        return response;
    }

    private Map<String, String> params = new HashMap<>();
    private DataObject data = null;
    private static volatile String apiKey;

    public Manager(String apiKey) {
        Manager.apiKey = apiKey;
    }

    /**
     * @return the params
     */
    public Map<String, String> getParams() {
        if (!params.containsKey("api_key")) {
            params.put("api_key", Manager.apiKey);
        }
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * @return the data
     */
    public DataObject getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(DataObject data) {
        this.data = data;
    }

    public String getJsonData() {
        if (getData() != null) {
            String serializedData = DataObjectSerializer.GSON_SERIALIZER.toJson(data);
            return serializedData;
        }
        return null;
    }

    protected String usersHostURL() {
        return APIEndPoints.GET_USERS_HOST + "?";
    }

    protected String addressBookURL() {
        return APIEndPoints.ADDRESSBOOK + "?";
    }

    protected String routeHostURL() {
        return APIEndPoints.ROUTE_HOST + "?";
    }

    protected String duplicateRoutetURL() {
        return APIEndPoints.DUPLICATE_ROUTE + "?";
    }

    protected String singleGeocoderURL() {
        return APIEndPoints.SINGLE_GEOCODER + "?";
    }

    protected String buildBaseURL() {
        return APIEndPoints.API_HOST + "?";
    }

    protected String routeURL() {
        return APIEndPoints.SHOW_ROUTE_HOST + "?";
    }

    protected String exportURL() {
        return APIEndPoints.EXPORTER + "?";
    }

    protected String batchGeocoderURL() {
        return APIEndPoints.BATCH_GEOCODER + "?";
    }

    protected String addressURL() {
        return APIEndPoints.ADDRESS_HOST + "?";
    }

    protected String addRouteNotesHostURL() {
        return APIEndPoints.ADD_ROUTE_NOTES_HOST + "?";
    }

    protected String avoidanceURL() {
        return APIEndPoints.AVOIDANCE + "?";
    }

    protected String moveRouteDestinationURL() {
        return APIEndPoints.MOVE_ROUTE_DESTINATION + "?";
    }

    protected String getActivityHostURL() {
        return APIEndPoints.GET_ACTIVITIES_HOST + "?";
    }

    protected String setGPSURL() {
        return APIEndPoints.SET_GPS_HOST + "?";
    }

}

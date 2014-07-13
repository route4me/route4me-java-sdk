package com.route4me.sdk;

import com.route4me.sdk.exception.APIConnectionException;
import com.route4me.sdk.exception.InvalidRequestException;
import com.route4me.sdk.model.DataObject;
import com.route4me.sdk.model.Optimization;
import com.route4me.sdk.model.Response;
import com.route4me.sdk.model.Route;
import com.route4me.sdk.model.SetGPS;
import static com.route4me.sdk.model.enums.APIEndPoints.API_HOST;
import static com.route4me.sdk.model.enums.APIEndPoints.MAIN_HOST;
import static com.route4me.sdk.model.enums.APIEndPoints.SHOW_ROUTE_HOST;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author juan
 */
public class Route4Me {

    public static final String CHARSET = "UTF-8";

    public static volatile String apiKey;

    private static CloseableHttpResponse createPostConnection(String url, String params, String data) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost(formatURL(url, params));
        post.setEntity(new StringEntity(data));
        CloseableHttpResponse response = null;
        response = httpClient.execute(post);
        return response;
    }

    private static CloseableHttpResponse createGetConnection(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(formatURL(url, params));
        CloseableHttpResponse response = null;
        response = httpClient.execute(get);
        return response;
    }

    private static CloseableHttpResponse createPutConnection(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut put = new HttpPut(formatURL(url, params));
        CloseableHttpResponse response = null;
        response = httpClient.execute(put);
        return response;
    }

    
    private static CloseableHttpResponse createDeleteConnection(String url, String params) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete delete = new HttpDelete(formatURL(url, params));
        CloseableHttpResponse response = null;
        response = httpClient.execute(delete);
        return response;
    }
    
    
    
    private final Optimization optimization;
    private final SetGPS setGPS;
    private final Route route;
    private final Response response = null;

    public Route4Me(String apiKey) {
        Route4Me.apiKey = apiKey;
        this.optimization = new Optimization(apiKey);
        this.setGPS = new SetGPS(apiKey);
        this.route = new Route(apiKey);
    }

    public String buildBaseURL() {
        return API_HOST + "?";
    }

    public String routeURL() {
        return SHOW_ROUTE_HOST + "?";
    }

    /**
     * @return the optimization
     */
    public Optimization getOptimization() {
        return optimization;
    }

    /**
     * @return the setGPS
     */
    public SetGPS getSetGPS() {
        return setGPS;
    }

    /**
     * @return the route
     */
    public Route getRoute() {
        return route;
    }

    /**
     * @return the response
     */
    public Response getResponse() {
        return response;
    }

    public Response reOptimization() {
        Response response = null;
        String params;
        try {
            params = transformParams(getRoute().getParams());
            response = makeURLConnectionRequest(RequestMethod.PUT,
                    buildBaseURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    protected enum RequestMethod {

        GET, POST, DELETE, PUT;
    }

    private static String urlEncode(String str) throws UnsupportedEncodingException {
        if (str == null) {
            return null;
        } else {
            return URLEncoder.encode(str, CHARSET);
        }
    }

    private static String urlEncodePair(String k, String v)
            throws UnsupportedEncodingException {
        return String.format("%s=%s", urlEncode(k), urlEncode(v));
    }

    private static String transformParams(Map<String, String> params)
            throws UnsupportedEncodingException, InvalidRequestException {
        StringBuilder queryStringBuffer = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (queryStringBuffer.length() > 0) {
                queryStringBuffer.append("&");
            }
            queryStringBuffer.append(urlEncodePair(entry.getKey(),
                    entry.getValue()));
        }
        return queryStringBuffer.toString();
    }

    private static String formatURL(String url, String query) {
        if (query == null || query.isEmpty()) {
            return url;
        } else {
            // In some cases, URL can already contain a question mark (eg, upcoming invoice lines)
            String separator = url.contains("?") ? "&" : "?";
            return String.format("%s%s%s", url, separator, query);
        }
    }

    private static Response makeURLConnectionRequest(
            RequestMethod method, String url, String params) throws APIConnectionException, IOException {
        return makeURLConnectionRequest(method, url, params, null);
    }

    private static Response makeURLConnectionRequest(
            RequestMethod method, String url, String params, String data) throws APIConnectionException, IOException {
        CloseableHttpResponse response = null;
        try {
            switch (method) {
                case GET:
                    response = createGetConnection(url, params);
                    break;
                case PUT:
                    response = createPutConnection(url, params);
                    break;
                case POST:
                    response = createPostConnection(url, params, data);
                    break;
                case DELETE:
                    response = createDeleteConnection(url, params);
                    break;
                default:
                    throw new APIConnectionException(
                            String.format(
                                    "Unrecognized HTTP method %s. ",
                                    method));
            }
            HttpEntity entity = response.getEntity();
            // trigger the request
            String responseBody = null;
            int statusCode = response.getStatusLine().getStatusCode();
            Header[] headers;
            if (statusCode >= 200 && statusCode < 300) {
                responseBody = getResponseBody(entity.getContent());
            } else if (entity.getContent() != null) {

                responseBody = getResponseBody(entity.getContent());
            }
            headers = response.getAllHeaders();
            String responseMessage = EntityUtils.toString(entity);
            return new Response(statusCode, responseBody, responseMessage, headers);

        } catch (IOException e) {
            throw new APIConnectionException(
                    String.format(
                            "Could not connect to Route4Me (%s). ", MAIN_HOST), e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    private static String getResponseBody(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                (inputStream)));

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


    public Response runOptimization() {
        Response response = null;
        String params;
        try {
            params = transformParams(getOptimization().getParams());
            DataObject data = getOptimization().getData();
            response = makeURLConnectionRequest(RequestMethod.POST,
                    buildBaseURL(), params, getOptimization().getJsonData());
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;

    }

    public Response getRouteFromAPI() {
        Response response = null;
        String params;
        try {
            params = transformParams(getRoute().getParams());
            response = makeURLConnectionRequest(RequestMethod.GET,
                    buildBaseURL(), params);
        } catch (APIConnectionException | InvalidRequestException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Route4Me.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;

    }


}

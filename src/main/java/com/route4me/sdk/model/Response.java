package com.route4me.sdk.model;

import org.apache.http.Header;

/**
 *
 * @author juan
 */
public class Response {

    private int responseCode;
    private String responseBody;
    private String responseMessage;
    private Header[] responseHeaders;

    public Response() {
    }

    
    public Response(int responseCode, String responseBody, String responseMessage, Header[] responseHeaders) {
        this.responseCode = responseCode;
        this.responseBody = responseBody;
        this.responseMessage = responseMessage;
        this.responseHeaders = responseHeaders;
    }
    
    

    /**
     * @return the responseCode
     */
    public int getResponseCode() {
        return responseCode;
    }

    /**
     * @param responseCode the responseCode to set
     */
    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    /**
     * @return the responseBody
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * @param responseBody the responseBody to set
     */
    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    /**
     * @return the responseHeaders
     */
    public Header[] getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * @param responseHeaders the responseHeaders to set
     */
    public void setResponseHeaders(Header[] responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    /**
     * @return the responseMessage
     */
    public String getResponseMessage() {
        return responseMessage;
    }

    /**
     * @param responseMessage the responseMessage to set
     */
    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

}

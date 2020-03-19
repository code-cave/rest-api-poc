package com.company.org.model;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class RequestVO implements Serializable {

    private static final long serialVersionUID = 1;
    public static final String REQUEST_BODY_ATTRIBUTE = "reqBodyAttrib";

    String inputReqBodyString;
    Map<String,String> inputHeaders;
    Map<String,String> inputPathVars;
    Map<String,String> requestBodyMap = new HashMap<>();
    String inputAcceptType;
    MediaType responseContentType = MediaType.APPLICATION_JSON;

    public RequestVO(String requestBody, Map<String,String> inputHeaders, Map<String,String> pathVars) {

        setLoggingAttribute(requestBody);
        this.inputReqBodyString =requestBody;
        cleanHeaders(inputHeaders);
        this.inputHeaders = inputHeaders;
        this.inputAcceptType = inputHeaders.get(HttpHeaders.ACCEPT);
        this.inputPathVars = pathVars;
    }

    public RequestVO(Map<String,String> requestBodyMap, Map<String,String> inputHeaders, Map<String,String> pathVars) {

        setLoggingAttribute(requestBodyMap);
        this.inputReqBodyString = "";
        this.requestBodyMap = requestBodyMap;
        cleanHeaders(inputHeaders);
        this.inputHeaders = inputHeaders;
        this.inputAcceptType = inputHeaders.get(HttpHeaders.ACCEPT);
        this.inputPathVars = pathVars;
    }

    static void setLoggingAttribute(Map<String,String> requestBody) {

        setLoggingAttribute(requestBody.toString());
    }

    static void setLoggingAttribute(String requestBody) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            request.setAttribute(REQUEST_BODY_ATTRIBUTE, requestBody);
        }
    }

    void cleanHeaders(Map<String, String> inputHeaders) {
        inputHeaders.put(HttpHeaders.CONTENT_TYPE, parseHeader(inputHeaders.get(HttpHeaders.CONTENT_TYPE)));
        inputHeaders.put(HttpHeaders.ACCEPT, parseHeader(inputHeaders.get(HttpHeaders.ACCEPT)));
    }

    boolean isJSONBased() {
        return MediaType.APPLICATION_JSON_VALUE.equals(parseHeader(inputHeaders.get(HttpHeaders.CONTENT_TYPE)));
    }

    private String parseHeader(String headerValue) {
        String[] values = StringUtils.split(headerValue, ";");
        return (values != null) ? values[0] : headerValue;
    }

    public MediaType getResponseContentType() {
        return responseContentType;
    }
}
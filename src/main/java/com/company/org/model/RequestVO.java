package com.company.org.model;

import org.apache.avro.specific.SpecificRecord;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Map;

public class RequestVO implements Serializable {

    private static final long serialVersionUID = 1;
    public static final String REQUEST_BODY_ATTRIBUTE = "reqBodyAttrib";

    String inputReqBodyString = "";
    Map<String,String> inputHeaders;
    Map<String,String> inputPathVars;
    SpecificRecord avroObject;
    // Can hard set this because all the APIs return json
    MediaType responseContentType = MediaType.APPLICATION_JSON;

    public RequestVO(Map<String,String> pathVars, Map<String, String> inputHeaders) {

        setLoggingAttribute(pathVars.toString());
        cleanHeaders(inputHeaders);
        this.inputHeaders = inputHeaders;
        this.inputPathVars = pathVars;
    }

    public RequestVO(String requestBody, Map<String, String> pathVars, Map<String, String> inputHeaders) {

        setLoggingAttribute(requestBody);
        this.inputReqBodyString = requestBody;
        cleanHeaders(inputHeaders);
        this.inputHeaders = inputHeaders;
        this.inputPathVars = pathVars;
    }

    private static void setLoggingAttribute(String requestBody) {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            request.setAttribute(REQUEST_BODY_ATTRIBUTE, requestBody);
        }
    }

    private void cleanHeaders(Map<String, String> inputHeaders) {

        if (inputHeaders != null) {
            inputHeaders.put(HttpHeaders.CONTENT_TYPE, parseHeader(inputHeaders.get(HttpHeaders.CONTENT_TYPE)));
            inputHeaders.put(HttpHeaders.ACCEPT, parseHeader(inputHeaders.get(HttpHeaders.ACCEPT)));
        }
    }

    public boolean hasJsonContentType() {

        return MediaType.APPLICATION_JSON_VALUE.equals(parseHeader(inputHeaders.get(HttpHeaders.CONTENT_TYPE)));
    }

    public boolean hasValidAcceptType() {

        String acceptType = parseHeader(inputHeaders.get(HttpHeaders.ACCEPT));
        return StringUtils.isEmpty(acceptType) ||
            MediaType.APPLICATION_JSON_VALUE.equals(acceptType) ||
            MediaType.ALL_VALUE.equals(acceptType);
    }

    private String parseHeader(String headerValue) {

        String[] values = StringUtils.split(headerValue, ";");
        return (values != null) ? values[0] : headerValue;
    }

    public void setAvroObject(SpecificRecord avroObject) {
        this.avroObject = avroObject;
    }

    public SpecificRecord getAvroObject() {
        return avroObject;
    }

    public String getInputReqBodyString() {
        return inputReqBodyString;
    }

    public MediaType getResponseContentType() {
        return responseContentType;
    }

    public Map<String, String> getInputHeaders() {
        return inputHeaders;
    }

    public Map<String, String> getPathVars() {
        return inputPathVars;
    }
}
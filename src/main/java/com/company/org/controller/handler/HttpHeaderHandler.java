package com.company.org.controller.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public final class HttpHeaderHandler {

    public static final String KEEP_ALIVE = "Keep-Alive";
    public static final String TIMEOUT_5 = "timeout=5";
    public static final HttpHeaders DEFAULT_RESPONSE_HEADERS = defaultResponseHeaders();

    public static HttpHeaders buildHeaders(MediaType responseContentType) {
        // Could change at some point
        HttpHeaders responseHeaders = new HttpHeaders();
        // Get the content type from the request
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, responseContentType.toString());
        // Set a keep alive so that transaction longer than 5 seconds timeout
        responseHeaders.add(KEEP_ALIVE, TIMEOUT_5);
        return responseHeaders;
    }

    private static HttpHeaders defaultResponseHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        // Hard set the response content type
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        // Set a keep alive so that transaction longer than 5 seconds timeout
        httpHeaders.add(KEEP_ALIVE, TIMEOUT_5);
        return httpHeaders;
    }
}

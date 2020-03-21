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
        responseHeaders.add(HttpHeaders.CONTENT_TYPE, responseContentType.toString());
        responseHeaders.add(KEEP_ALIVE, TIMEOUT_5);
        return responseHeaders;
    }

    private static HttpHeaders defaultResponseHeaders() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
        httpHeaders.add(KEEP_ALIVE, TIMEOUT_5);
        return httpHeaders;
    }
}

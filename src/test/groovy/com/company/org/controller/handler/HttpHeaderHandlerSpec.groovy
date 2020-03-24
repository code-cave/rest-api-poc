package com.company.org.controller.handler

import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import spock.lang.Specification

class HttpHeaderHandlerSpec extends Specification {

    def 'Test constants'() {

        when:
        HttpHeaderHandler httpHeaderHandler = new HttpHeaderHandler()
        HttpHeaders defaultHeaders = new HttpHeaders()
        defaultHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        defaultHeaders.add(httpHeaderHandler.KEEP_ALIVE, 'timeout=5')

        then:
        httpHeaderHandler.KEEP_ALIVE == 'Keep-Alive'
        httpHeaderHandler.TIMEOUT_5 == 'timeout=5'
        httpHeaderHandler.DEFAULT_RESPONSE_HEADERS == defaultHeaders
    }

    def 'Test buildHeaders'() {

        setup:
        HttpHeaderHandler httpHeaderHandler = new HttpHeaderHandler()
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        headers.add(httpHeaderHandler.KEEP_ALIVE, 'timeout=5')

        when:
        HttpHeaders buildHeaders = httpHeaderHandler.buildHeaders(MediaType.APPLICATION_JSON)

        then:
        buildHeaders == headers
    }

    def 'Test defaultResponseHeaders'() {

        setup:
        HttpHeaderHandler httpHeaderHandler = new HttpHeaderHandler()
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        headers.add(httpHeaderHandler.KEEP_ALIVE, 'timeout=5')

        when:
        HttpHeaders defaultResponseHeaders = httpHeaderHandler.defaultResponseHeaders()

        then:
        defaultResponseHeaders == headers
    }
}

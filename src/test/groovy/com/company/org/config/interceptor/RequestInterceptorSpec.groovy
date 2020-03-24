package com.company.org.config.interceptor

import spock.lang.Specification

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RequestInterceptorSpec extends Specification {

    def 'Test preHandle'() {

        setup:
        RequestInterceptor requestInterceptor = new RequestInterceptor()
        HttpServletRequest httpServletRequest = Mock()
        HttpServletResponse httpServletResponse = Mock()

        when:
        boolean preHandle = requestInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object())

        then:
        1 * httpServletRequest.getAttribute('start_time') >> 112233445566
        preHandle
    }

    def 'Test preHandle start_time is null'() {

        setup:
        RequestInterceptor requestInterceptor = new RequestInterceptor()
        HttpServletRequest httpServletRequest = Mock()
        HttpServletResponse httpServletResponse = Mock()

        when:
        boolean preHandle = requestInterceptor.preHandle(httpServletRequest, httpServletResponse, new Object())

        then:
        1 * httpServletRequest.getAttribute('start_time') >> null
        preHandle
    }

    def 'Test afterCompletion'() {

        setup:
        RequestInterceptor requestInterceptor = new RequestInterceptor()
        HttpServletRequest httpServletRequest = Mock()
        HttpServletResponse httpServletResponse = Mock()
        long startTime = 1584986299000

        when:
        def afterCompletion = requestInterceptor.afterCompletion(httpServletRequest, httpServletResponse, new Object(), new Exception())

        then:
        1 * httpServletRequest.getAttribute('start_time') >> startTime
        afterCompletion == null
    }
}

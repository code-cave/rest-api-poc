package com.company.org.config.logging

import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class LoggingAspectSpec extends Specification {

    def 'Test logBefore'() {

        setup:
        LoggingAspect loggingAspect = new LoggingAspect()
        HttpServletRequest httpServletRequest = Stub(HttpServletRequest)
        httpServletRequest.getRequestURI() >> "/api/c360/individual/crosswalk/v1.0/read"
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(httpServletRequest));

        when:
        loggingAspect.logBefore()

        then:
        httpServletRequest.getHeader('token') >> '112233'
        httpServletRequest.setAttribute('start_time', _)
        loggingAspect != null
    }

    def "Test getParamString"() {

        setup:
        LoggingAspect loggingAspectConfiguration = new LoggingAspect()
        Map<String, String[]> parameterMap = new HashMap<>()
        parameterMap.put('foo', [ 'bar', 'baz' ] as String[])
        String actualString = 'foo=bar, foo=baz'

        when:
        String expectedString = loggingAspectConfiguration.getParamString(parameterMap)

        then:
        expectedString == actualString
    }
}

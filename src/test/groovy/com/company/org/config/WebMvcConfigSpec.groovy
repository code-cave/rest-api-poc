package com.company.org.config

import org.springframework.web.servlet.config.annotation.InterceptorRegistration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import spock.lang.Specification

class WebMvcConfigSpec extends Specification {

    def "Test addInterceptors"() {

        setup:
        WebMvcConfig webMvcConfig = new WebMvcConfig()
        InterceptorRegistry registry = Stub(InterceptorRegistry)
        InterceptorRegistration interceptorRegistration = Stub(InterceptorRegistration)
        interceptorRegistration.addPathPatterns("/api/**") >> Mock(InterceptorRegistration)

        when:
        webMvcConfig.addInterceptors(registry)

        then:
        notThrown(Exception)
    }
}

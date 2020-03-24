package com.company.org.config;

import com.company.org.config.interceptor.RequestInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// This class intercepts all incoming requests
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    RequestInterceptor requestInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Add in the interceptor to the registry so it can be used
        // Match all API paths so the info is collected for each API
        registry.addInterceptor(requestInterceptor).addPathPatterns("/api/**");
    }
}
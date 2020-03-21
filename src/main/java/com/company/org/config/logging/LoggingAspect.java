package com.company.org.config.logging;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Aspect
@Configuration
public class LoggingAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.company.org.controller.*.*(..))")
    public void logBefore() {

        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest req = attributes.getRequest();
        String token = req.getHeader("token");

        Long startTime = new Timestamp(System.currentTimeMillis()).getTime();
        req.setAttribute("start_time", startTime);

        String logMessage = new StringBuilder()
            .append("[===== STARTING ").append(req.getRequestURI())
            .append(", TOKEN=").append(token)
            .append(", TS=").append(startTime).toString();

        String paramsLogMessage = new StringBuilder()
            .append("[===== PARAMS FOR ").append(req.getRequestURI())
            .append(", TOKEN=").append(token)
            .append(", TS=").append(startTime)
            .append(", ").append(getParamString(req.getParameterMap())).toString();

        LOGGER.info(logMessage);
        LOGGER.info(paramsLogMessage);
    }

    protected String getParamString(Map<String, String[]> parameterMap) {

        List<String> paramList = new ArrayList<>();
        if (!parameterMap.isEmpty()) {
            Set<String> keySet = parameterMap.keySet();
            for (String key : keySet) {
                String[] strings = parameterMap.get(key);
                for (String string : strings) {
                    paramList.add(key + "=" + string);
                }
            }
        }

        return String.join(", ", paramList);
    }
}
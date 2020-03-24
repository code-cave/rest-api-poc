package com.company.org.config.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;

// This class intercepts all incoming http requests.
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object o) {
        // Adding in super simple auth
        // Can always be replaced with something legit
        if (req.getAttribute("start_time") == null) {
            // Make sure there is always a timestamp that can be logged
            Long startTime = new Timestamp(System.currentTimeMillis()).getTime();
            req.setAttribute("start_time", startTime);
        }

        return true;
    }

    // Do some things after dishing out the response
    @Override
    public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object o, Exception ex) {
        // Construct the info for logging requests whose values
        // can be offloaded onto logging apps like Kibana
        Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        long endTime = timeStamp.getTime();
        Long startTime = (Long) req.getAttribute("start_time");
        long elapsedTime = endTime - startTime;
        // Set a capacity since the know length is relatively known
        String logMessage = new StringBuilder(100)
            .append("[===== ENDING ").append(req.getRequestURI())
            .append(", AUTHTOKEN=").append(req.getHeader("token"))
            .append(", TS=").append(endTime)
            .append(", TIME=").append(elapsedTime)
            .append(", CODE=").append(res.getStatus()).toString();

        LOGGER.info(logMessage);
    }
}

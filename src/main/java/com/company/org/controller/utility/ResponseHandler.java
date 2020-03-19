package com.company.org.controller.utility;

import com.company.org.error.ErrorVO;
import com.company.org.model.ResponseVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResponseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResponseHandler.class);

    public ResponseEntity<String> createResponse(ResponseVO responseVO) {

        return new ResponseEntity<>(responseVO.getResponseStr(), HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS, HttpStatus.OK);
    }

    public ResponseEntity<String> createErrorResponse(ErrorVO errorVO) {

        String responseStr = buildErrorResponse(errorVO);

        return new ResponseEntity<>(responseStr, HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS, errorVO.getHttpStatus());
    }

    ResponseEntity<String> successResponseDefaultHeaders(String responseStr){
        return new ResponseEntity<>(responseStr, HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS, HttpStatus.OK);
    }

    ResponseEntity<String> successResponse(String responseStr, HttpHeaders responseHeaders){
        return new ResponseEntity<>(responseStr, responseHeaders, HttpStatus.OK);
    }

    private String buildErrorResponse(ErrorVO errorVO) {
        return new StringBuilder()
            .append('{')
            .append("\"timestamp\":\"").append(new Date().toString()).append("\",")
            .append("\"status\":").append(errorVO.getHttpStatus().value()).append(",")
            .append("\"error\":\"").append(errorVO.getHttpStatus().getReasonPhrase()).append("\",")
            .append("\"message\":\"").append(errorVO.getErrorMessage()).append('"')
            .append('}')
            .toString();
    }
}

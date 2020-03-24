package com.company.org.controller.handler;

import com.company.org.error.ErrorVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class ResponseHandler {

    public ResponseEntity<String> createResponse(ResponseVO responseVO) {

        return new ResponseEntity<>(responseVO.getResponseStr(), HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS, HttpStatus.OK);
    }

    public ResponseEntity<String> createErrorResponse(ErrorVO errorVO) {

        String responseStr = buildErrorResponse(errorVO);

        return new ResponseEntity<>(responseStr, HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS, errorVO.getHttpStatus());
    }

    private String buildErrorResponse(ErrorVO errorVO) {

        return new ErrorResponse(
            new Date().toString(),
            errorVO.getHttpStatus().value(),
            errorVO.getHttpStatus().getReasonPhrase(),
            errorVO.getErrorMessage()
        ).toString();
    }
}

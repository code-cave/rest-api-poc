package com.company.org.model;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ResponseVO implements Serializable {

    private static final long serialVersionUID = 1;

    private String responseStr;
    private Object responseObject;
    private HttpStatus httpStatus = HttpStatus.OK;

    public ResponseVO(String responseStr) {
        this.responseStr = responseStr;
    }

    public ResponseVO(Object responseObj) {
        this.responseObject = responseObj;
    }

    public ResponseVO(String responseStr, int statusCode) {
        this.responseStr = responseStr;
        this.httpStatus = HttpStatus.valueOf(statusCode);
    }

    public String getResponseStr() {
        return this.responseStr;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public Object getResponseObject() {
        return this.responseObject;
    }
}
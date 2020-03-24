package com.company.org.error;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class ErrorVO implements Serializable{

    private static final long serialVersionUID = 1;

    private HttpStatus httpStatus;
    private String errorMessage;

    // A POJO that is a VO for holding API error info
    public ErrorVO(HttpStatus httpStatus, String errorMessage) {
          this.httpStatus = httpStatus;
          this.errorMessage = errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String buildErrorMessage() {
        return  "(" + httpStatus.getReasonPhrase() + "): " + errorMessage;
    }
}

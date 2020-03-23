package com.company.org.exception;

import com.company.org.error.ErrorVO;

public class BaseRuntimeException extends RuntimeException {

    private final ErrorVO errorVO;

    public BaseRuntimeException(ErrorVO errorVO) {
        super(errorVO.getErrorMessage());
        this.errorVO = errorVO;
    }

    public BaseRuntimeException(ErrorVO errorVO, Throwable cause) {
        super(errorVO.getErrorMessage(), cause);
        this.errorVO = errorVO;
    }

    public ErrorVO getErrorVO() {
        return errorVO;
    }

    public int getStatusCode() {
        return errorVO.getHttpStatus().value();
    }

    public String getErrorMessage() {
        return  errorVO.buildErrorMessage();
    }
}
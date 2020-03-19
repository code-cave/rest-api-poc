package com.company.org.exception;

import com.company.org.error.ErrorVO;

public class BaseRuntimeException extends RuntimeException {

    private final ErrorVO errorVO;

    public BaseRuntimeException(ErrorVO errorVO) {
        super(errorVO.buildErrorMessage());
        this.errorVO = errorVO;
    }

    public BaseRuntimeException(ErrorVO errorVO, Throwable cause) {
        super(errorVO.buildErrorMessage(), cause);
        this.errorVO = errorVO;
    }
}
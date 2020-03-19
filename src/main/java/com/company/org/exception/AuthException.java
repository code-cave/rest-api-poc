package com.company.org.exception;

import com.company.org.error.ErrorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class AuthException extends BaseRuntimeException {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthException.class);

    public AuthException(HttpStatus httpStatus, String errorMessage) {
        super(new ErrorVO(httpStatus, errorMessage));
        LOGGER.error(httpStatus.getReasonPhrase() + " error: " + errorMessage);
    }

    public AuthException(HttpStatus httpStatus, String errorMessage, Throwable cause){
        super(new ErrorVO(httpStatus, errorMessage), cause);
        LOGGER.error(errorMessage, cause);
    }
}
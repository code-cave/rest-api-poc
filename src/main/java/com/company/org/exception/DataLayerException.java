package com.company.org.exception;

import com.company.org.error.ErrorVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

public class DataLayerException extends BaseRuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(DataLayerException.class);

    public DataLayerException(HttpStatus httpStatus, String errorMessage) {
        super(new ErrorVO(httpStatus, errorMessage));
     }

    public DataLayerException(HttpStatus httpStatus, String errorMessage, Throwable cause) {
        super( new ErrorVO(httpStatus, errorMessage),cause);
        if (HttpStatus.INTERNAL_SERVER_ERROR == httpStatus) {
            LOGGER.error(errorMessage, cause);
        }
    }
}

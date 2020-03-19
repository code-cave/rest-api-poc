package com.company.org.exception;

import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ServiceLayerException extends BaseRuntimeException{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLayerException.class);

    private final RequestVO requestVO;

    public ServiceLayerException(HttpStatus httpStatus, String errorDesc) {

        super(new ErrorVO(httpStatus, errorDesc));
        this.requestVO = null;
    }

    public ServiceLayerException(HttpStatus httpStatus, String errorDesc, Throwable cause) {

        super(new ErrorVO(httpStatus, errorDesc), cause);
        this.requestVO = null;
        if (HttpStatus.INTERNAL_SERVER_ERROR == httpStatus) {
            LOGGER.error(errorDesc, cause);
        }
    }

    public ServiceLayerException(HttpStatus httpStatus, String errorDesc, RequestVO requestVO) {

        super(new ErrorVO(httpStatus, errorDesc));
        this.requestVO = requestVO;
    }

    public ServiceLayerException(HttpStatus httpStatus, String errorDesc, Throwable cause, RequestVO requestVO) {

        super(new ErrorVO(httpStatus, errorDesc), cause);
        if (HttpStatus.INTERNAL_SERVER_ERROR == httpStatus) {
            LOGGER.error(errorDesc, cause);
        }
        this.requestVO = requestVO;
    }

    public MediaType getResponseContentType() {

        return (requestVO != null) ? requestVO.getResponseContentType() : null;
    }
}

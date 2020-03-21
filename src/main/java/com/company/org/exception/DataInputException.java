package com.company.org.exception;

import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;
import org.springframework.http.MediaType;

public class DataInputException extends BaseRuntimeException {

    private final MediaType responseContentType;

    public DataInputException(ErrorVO errorVO, RequestVO requestVO){
        this(errorVO, requestVO.getResponseContentType());
    }

    public DataInputException(ErrorVO errorVO, RequestVO requestVO, Throwable cause){
        this(errorVO, requestVO.getResponseContentType(), cause);
    }

    public DataInputException(ErrorVO errorVO, MediaType responseContentType){
        super(errorVO);
        this.responseContentType = responseContentType;
    }

    public DataInputException(ErrorVO errorVO, MediaType responseContentType, Throwable cause){
        super(errorVO, cause);
        this.responseContentType = responseContentType;
    }
}

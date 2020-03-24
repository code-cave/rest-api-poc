package com.company.org.exception;

import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;
import org.springframework.http.MediaType;

public class DataInputException extends BaseRuntimeException {

    public DataInputException(ErrorVO errorVO, RequestVO requestVO) {
        this(errorVO, requestVO.getResponseContentType());
    }

    public DataInputException(ErrorVO errorVO, RequestVO requestVO, Throwable cause) {
        this(errorVO, requestVO.getResponseContentType(), cause);
    }
    // These are here in case there is a need to handle
    // different response content types in the future
    public DataInputException(ErrorVO errorVO, MediaType responseContentType) {
        super(errorVO);
    }

    public DataInputException(ErrorVO errorVO, MediaType responseContentType, Throwable cause) {
        super(errorVO, cause);
    }
}

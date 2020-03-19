package com.company.org.exception;

import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;


public class FailFastException extends DataInputException {

    public FailFastException(ErrorVO errorVO, RequestVO requestVO) {
        super(errorVO, requestVO);
    }

    public FailFastException(ErrorVO errorVO, RequestVO requestVO, Throwable cause) {
        super(errorVO, requestVO, cause);
    }
}

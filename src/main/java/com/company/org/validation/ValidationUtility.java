package com.company.org.validation;

import com.company.org.error.ErrorVO;
import com.company.org.exception.FailFastException;
import com.company.org.model.RequestVO;
import org.springframework.http.HttpStatus;

public final class ValidationUtility {

    public static void failFast(HttpStatus httpStatus, String errorDesc, RequestVO requestVO) {
        throw new FailFastException(new ErrorVO(httpStatus, errorDesc), requestVO);
    }

    public static void failFast(HttpStatus httpStatus, String errorDesc, RequestVO requestVO, Exception exception) {
        throw new FailFastException(new ErrorVO(httpStatus, errorDesc), requestVO, exception);
    }
}

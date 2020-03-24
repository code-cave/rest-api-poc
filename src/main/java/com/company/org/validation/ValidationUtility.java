package com.company.org.validation;

import com.company.org.error.ErrorVO;
import com.company.org.exception.FailFastException;
import com.company.org.model.RequestVO;
import org.springframework.http.HttpStatus;

import java.util.regex.Pattern;

// Just a utility for dealing with validation errors
public final class ValidationUtility {

    private static final Pattern CURRENCY = Pattern.compile("(([1-9]\\d*)?\\d)(\\.\\d{0,2})$");

    private static final String INVALID_CURRENCY_VALUE = "Invalid format for currency value";

    public static void failFast(HttpStatus httpStatus, String errorDesc, RequestVO requestVO) {
        throw new FailFastException(new ErrorVO(httpStatus, errorDesc), requestVO);
    }

    public static void failFast(HttpStatus httpStatus, String errorDesc, RequestVO requestVO, Exception exception) {
        throw new FailFastException(new ErrorVO(httpStatus, errorDesc), requestVO, exception);
    }

    public static void validateCurrencyValue(RequestVO requestVO, double value) {
        // There could be trailing 0s
        String str = String.valueOf(value);
        if (!CURRENCY.matcher(str).matches()) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_CURRENCY_VALUE, requestVO);
        }
    }
}

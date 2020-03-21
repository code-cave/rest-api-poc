package com.company.org.validation;

import com.company.org.exception.DataInputException;
import com.company.org.model.RequestVO;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface RequestValidatorBase {

    void validateRequest(RequestVO requestVO) throws DataInputException;

    default RequestVO validateGetRequest(Map<String, String> pathVars, Map<String,String> inputHeaders) {

        RequestVO requestVO = new RequestVO(pathVars, inputHeaders);
        validateHeaders(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default RequestVO validatePostRequest(String requestBody, Map<String,String> inputHeaders) {

        RequestVO requestVO = new RequestVO(requestBody, null, inputHeaders);
        validateHeaders(requestVO);
        validateContentType(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default RequestVO validatePutRequest(String requestBody, Map<String, String> pathVars, Map<String, String> inputHeaders) {

        RequestVO requestVO = new RequestVO(requestBody, pathVars, inputHeaders);

        validateHeaders(requestVO);
        validateContentType(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default HttpStatus getDefaultErrorStatusCode() {

        return HttpStatus.BAD_REQUEST;
    }

    default void validateContentType(RequestVO requestVO) {

       if (!requestVO.hasJsonContentType()) {
           ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Bad Content Type in request", requestVO);
       }
    }

    default void validateAccept(RequestVO requestVO) {

        if (!requestVO.hasValidAcceptType()) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Bad Accept Type in request", requestVO);
        }
    }

    default void validateHeaders(RequestVO requestVO) {

        if (requestVO.getInputHeaders() == null) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Required headers missing", requestVO);
        }
    }
}

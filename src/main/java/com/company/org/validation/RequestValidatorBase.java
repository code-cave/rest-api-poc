package com.company.org.validation;

import com.company.org.exception.DataInputException;
import com.company.org.model.RequestVO;
import org.springframework.http.HttpStatus;

import java.util.Map;

public interface RequestValidatorBase {

    // Implement this method for each validator so that specific
    // logic can be applied to each of the different APIs
    void validateRequest(RequestVO requestVO) throws DataInputException;

    default RequestVO validateGetRequest(Map<String, String> pathVars, Map<String,String> inputHeaders) {
        // Need to validate headers and the accept for get requests
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders);
        validateHeaders(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default RequestVO validatePostRequest(String requestBody, Map<String,String> inputHeaders) {
        // Need to validate headers, content type, and the accept for post requests
        RequestVO requestVO = new RequestVO(requestBody, null, inputHeaders);
        validateHeaders(requestVO);
        validateContentType(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default RequestVO validatePutRequest(String requestBody, Map<String, String> pathVars, Map<String, String> inputHeaders) {
        // Need to validate headers, content type, and the accept for put requests
        RequestVO requestVO = new RequestVO(requestBody, pathVars, inputHeaders);
        validateHeaders(requestVO);
        validateContentType(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default RequestVO validateDeleteRequest(Map<String, String> pathVars, Map<String,String> inputHeaders) {
        // Need to validate headers and the accept for delete requests
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders);
        validateHeaders(requestVO);
        validateAccept(requestVO);
        validateRequest(requestVO);

        return requestVO;
    }

    default void validateContentType(RequestVO requestVO) {
        // Should always be application/json
       if (!requestVO.hasJsonContentType()) {
           ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Bad Content Type in request", requestVO);
       }
    }

    default void validateAccept(RequestVO requestVO) {
        // Should always be application/json
        if (!requestVO.hasValidAcceptType()) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Bad Accept Type in request", requestVO);
        }
    }

    default void validateHeaders(RequestVO requestVO) {
        // No headers mean not good
        if (requestVO.getInputHeaders() == null) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, "Required headers missing", requestVO);
        }
    }
}

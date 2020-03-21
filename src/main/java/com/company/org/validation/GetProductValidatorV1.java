package com.company.org.validation;

import com.company.org.model.RequestVO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetProductValidatorV1 implements RequestValidatorBase {

    private static final String INVALID_ID_PARAMETER = "Invalid format for id path parameter";

    @Override
    public void validateRequest(RequestVO requestVO) {
        // Pretty simple, not a whole lot going on here
        // Just making sure to check that the id is actually of type long
        try {
            Long.parseLong(requestVO.getPathVars().get("id"));
        }
        catch (NumberFormatException e) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_ID_PARAMETER, requestVO);
        }
    }
}

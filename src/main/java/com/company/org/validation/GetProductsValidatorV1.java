package com.company.org.validation;

import com.company.org.model.RequestVO;
import org.springframework.stereotype.Component;

@Component
public class GetProductsValidatorV1 implements RequestValidatorBase {

    @Override
    public void validateRequest(RequestVO requestVO) {
        // Nothing to validate here, Accept header already checked
        // Just a pass through in case something needs to be added later
    }
}

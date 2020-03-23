package com.company.org.validation

import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import spock.lang.Specification

class GetProductsValidatorV1Spec extends Specification {

    Map headers = [ (HttpHeaders.ACCEPT): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test pass through method'() {

        given:
        GetProductsValidatorV1 validator = new GetProductsValidatorV1()

        when:
        FailFastException validate = validator.validateRequest(new RequestVO([:], headers))

        then:
        validate == null
    }
}

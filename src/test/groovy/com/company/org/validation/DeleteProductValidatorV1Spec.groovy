package com.company.org.validation

import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class DeleteProductValidatorV1Spec extends Specification {

    Map headers = [ (HttpHeaders.ACCEPT): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test constants'() {

        when:
        DeleteProductValidatorV1 validator = new DeleteProductValidatorV1()

        then:
        validator.INVALID_ID_PARAMETER == 'Invalid format for id path parameter'
    }

    def 'Test positive cases'() {

        given:
        DeleteProductValidatorV1 validator = new DeleteProductValidatorV1()
        Map pathVars = [ 'id': id ]

        when:
        FailFastException validate = validator.validateRequest(new RequestVO(pathVars, headers))

        then:
        validate == expectedMessage

        where:
        id       | expectedMessage
        '1'      | null
        '000'    | null
        '123'    | null
        '112924' | null
    }

    def 'Test negative cases'() {

        given:
        DeleteProductValidatorV1 validator = new DeleteProductValidatorV1()
        Map pathVars = [ 'id': id ]

        when:
        validator.validateRequest(new RequestVO(pathVars, headers))

        then:
        FailFastException validate = thrown(FailFastException)
        validate.getStatusCode() == statusCode
        validate.getMessage() == expectedMessage

        where:
        id     | statusCode                     | expectedMessage
        ''     | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
        'aa'   | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
        'a123' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
        '(123' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
    }
}

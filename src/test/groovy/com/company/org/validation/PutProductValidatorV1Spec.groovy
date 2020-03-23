package com.company.org.validation

import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class PutProductValidatorV1Spec extends Specification {

    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]

    def 'Test constants'() {

        when:
        PutProductValidatorV1 validator = new PutProductValidatorV1()

        then:
        validator.INVALID_ID_PARAMETER == 'Invalid format for id path parameter'
    }

    def 'Test positive cases'() {

        given:
        PutProductValidatorV1 validator = new PutProductValidatorV1()
        Map pathVars = [ 'id': id ]
        String requestBody = new Product(avroId, name, new CurrentPrice(value, 'USD')).toString()

        when:
        FailFastException validate = validator.validateRequest(new RequestVO(requestBody, pathVars, headers))

        then:
        validate == expectedMessage

        where:
        id       | avroId | name  | value | expectedMessage
        '1'      | 1      | 'foo' | 12.0  | null
        '000'    | 000    | 'bar' | 12.9  | null
        '123'    | 123    | 'baz' | 12.00 | null
        '112924' | 112924 | 'biz' | 12.01 | null
    }

    def 'Test negative cases'() {

        given:
        PutProductValidatorV1 validator = new PutProductValidatorV1()
        Map pathVars = [ 'id': id ]
        String requestBody = '{ "_id": ' + avroId + ', "name": ' + name +
            ', "current_price": { "value": ' + value + ', "currency_code": "USD" } }'
        when:
        validator.validateRequest(new RequestVO(requestBody, pathVars, headers))

        then:
        FailFastException validate = thrown(FailFastException)
        validate.getStatusCode() == statusCode
        validate.getMessage() == expectedMessage

        where:
        id    | avroId | name    | value   | statusCode                     | expectedMessage
        '1'   | '2'    | '"foo"' | '12.99' | HttpStatus.BAD_REQUEST.value() | 'Invalid request body id does not match path id'
        'aa'  | 'aa'   | '"foo"' | '12.99' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
        '1.2' | '1.2'  | '"foo"' | '12.99' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for id path parameter'
        '000' | '000'  | '"bar"' | '.99'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '123' | '123'  | '"bar"' | '.99'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '111' | '111'  | '123'   | '123'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '111' | '111'  | '"baz"' | '0.999' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for currency value'
    }
}

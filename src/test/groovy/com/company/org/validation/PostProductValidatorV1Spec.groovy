package com.company.org.validation

import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class PostProductValidatorV1Spec extends Specification {

    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]

    def 'Test constants'() {

        when:
        PostProductValidatorV1 validator = new PostProductValidatorV1()

        then:
        validator.INVALID_REQUEST_BODY == 'Invalid request body object not schema compliant'
    }

    def 'Test positive cases'() {

        given:
        PostProductValidatorV1 validator = new PostProductValidatorV1()
        String requestBody = new Product(id, name, new CurrentPrice(value, 'USD')).toString()

        when:
        FailFastException validate = validator.validateRequest(new RequestVO(requestBody, null, headers))

        then:
        validate == expectedMessage

        where:
        id     | name  | value | expectedMessage
        1      | 'foo' | 12.0  | null
        1      | 'foo' | 0.99  | null
        000    | 'bar' | 12.9  | null
        123    | 'baz' | 12.00 | null
        112924 | 'biz' | 12.01 | null
    }

    def 'Test negative cases'() {

        given:
        PostProductValidatorV1 validator = new PostProductValidatorV1()
        String requestBody = '{ "_id": ' + id + ', "name": ' + name +
            ', "current_price": { "value": ' + value + ', "currency_code": "USD" } }'
        when:
        validator.validateRequest(new RequestVO(requestBody, null, headers))

        then:
        FailFastException validate = thrown(FailFastException)
        validate.getStatusCode() == statusCode
        validate.getMessage() == expectedMessage

        where:
        id    | name    | value   | statusCode                     | expectedMessage
        'aa'  | '"foo"' | '12.99' | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '000' | '"bar"' | '.99'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '123' | '"bar"' | '.99'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '111' | '123'   | '123'   | HttpStatus.BAD_REQUEST.value() | 'Invalid request body object not schema compliant'
        '111' | '"baz"' | '0.999' | HttpStatus.BAD_REQUEST.value() | 'Invalid format for currency value'
    }
}

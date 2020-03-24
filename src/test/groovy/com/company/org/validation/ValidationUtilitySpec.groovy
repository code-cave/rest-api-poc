package com.company.org.validation

import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

import java.util.regex.Pattern

class ValidationUtilitySpec extends Specification {

    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]

    def 'Test constants'() {

        setup:
        String pattern = Pattern.compile('(([1-9]\\d*)?\\d)(\\.\\d{0,2})$').toString()

        when:
        ValidationUtility validationUtility = new ValidationUtility()

        then:
        validationUtility.CURRENCY.toString() == pattern
        validationUtility.INVALID_CURRENCY_VALUE == 'Invalid format for currency value'
    }

    def 'Test failFast case 1'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        ValidationUtility validationUtility = new ValidationUtility()

        when:
        validationUtility.failFast(HttpStatus.BAD_REQUEST, 'bad request dude', requestVO)

        then:
        thrown(FailFastException)
    }

    def 'Test failFast case 2'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        ValidationUtility validationUtility = new ValidationUtility()

        when:
        validationUtility.failFast(HttpStatus.BAD_REQUEST, 'bad request dude', requestVO, new Exception('foo'))

        then:
        thrown(FailFastException)
    }

    def 'Test validateCurrencyValue success'() {

        setup:

        when:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        ValidationUtility validationUtility = new ValidationUtility()
        Double price = new Double(value)

        then:
        validationUtility.validateCurrencyValue(requestVO, price) == result

        where:
        value   | result
        '12.29' | null
        '02.29' | null
        '.29'   | null
        '0.1'   | null
        '0.01'  | null
        '0.00'  | null
        '0.000' | null
        '.0'    | null
    }

    def 'Test validateCurrencyValue fail'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        ValidationUtility validationUtility = new ValidationUtility()
        Double price = new Double('.999')

        when:
        validationUtility.validateCurrencyValue(requestVO, price)

        then:
        thrown(FailFastException)
    }
}

package com.company.org.validation

import com.company.org.exception.DataInputException
import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import spock.lang.Specification

class RequestValidatorBaseSpec extends Specification {

    private class RequestValidatorBaseTester implements RequestValidatorBase {

        @Override
        void validateRequest(RequestVO requestDataVO) throws DataInputException {
        }
    }
    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]
    Map invalidHeaders = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_XML_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_XML_VALUE
    ]

    def 'Test validateGetRequest'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateGetRequest(pathVars, headers)

        then:
        notThrown(FailFastException)
    }

    def 'Test validatePostRequest'() {

        setup:
        long id = 123
        String requestBody = new Product(id, 'cool product', new CurrentPrice(12.99, 'USD')).toString()
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validatePostRequest(requestBody, headers)

        then:
        notThrown(FailFastException)
    }

    def 'Test validatePutRequest'() {

        setup:
        long id = 123
        Map pathVars = [ 'id': '123' ]
        String requestBody = new Product(id, 'cool product', new CurrentPrice(12.99, 'USD')).toString()
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validatePutRequest(requestBody, pathVars, headers)

        then:
        notThrown(FailFastException)
    }

    def 'Test validateDeleteRequest'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateDeleteRequest(pathVars, headers)

        then:
        notThrown(FailFastException)
    }

    def 'Test validateContentType success'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateContentType(requestVO)

        then:
        notThrown(FailFastException)
    }

    def 'Test validateContentType fail'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, invalidHeaders)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateContentType(requestVO)

        then:
        thrown(FailFastException)
    }

    def 'Test validateAccept success'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateAccept(requestVO)

        then:
        notThrown(FailFastException)
    }

    def 'Test validateAccept fail'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, invalidHeaders)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateAccept(requestVO)

        then:
        thrown(FailFastException)
    }

    def 'Test validateHeaders success'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateHeaders(requestVO)

        then:
        notThrown(FailFastException)
    }

    def 'Test validateHeaders fail'() {

        setup:
        Map pathVars = [ 'id': '123' ]
        RequestVO requestVO = new RequestVO(pathVars, null)
        RequestValidatorBase requestValidatorBaseTester = new RequestValidatorBaseTester()

        when:
        requestValidatorBaseTester.validateHeaders(requestVO)

        then:
        thrown(FailFastException)
    }
}

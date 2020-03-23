package com.company.org.controller

import com.company.org.controller.handler.ResponseHandler
import com.company.org.error.ErrorVO
import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.security.Authentication
import com.company.org.service.PostProductServiceV1
import com.company.org.validation.PostProductValidatorV1
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class PostProductControllerV1Spec extends Specification {

    PostProductControllerV1 controller = new PostProductControllerV1()
    Authentication authentication = Mock()
    PostProductValidatorV1 postProductValidatorV1 = Mock()
    PostProductServiceV1 postProductServiceV1 = Mock()
    ResponseHandler responseHandler = Mock()
    String applicationJson = MediaType.APPLICATION_JSON_VALUE
    String requestBody = '''{
        "_id": 123,
        "name": "Some cool item",
        "current_price": {
            "value": 13.99,
            "currency_code": "USD"
        }
    }'''
    String requestBodyBad = '''{
        "_id": "OOPS",
        "name": "Some cool item",
        "current_price": {
            "value": 13.99,
            "currency_code": "USD"
        }
    }'''

    def 'Test postProduct returns a result'() {

        setup:
        controller.authentication = authentication
        controller.postProductValidatorV1 = postProductValidatorV1
        controller.postProductServiceV1 = postProductServiceV1
        controller.responseHandler = responseHandler
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(requestBody)
        RequestVO requestVO = new RequestVO(requestBody, null, buildHeaders().toSingleValueMap())
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), buildHeaders(), HttpStatus.OK)

        when:
        ResponseEntity<String> responseEntity = controller.postProduct(token, applicationJson, applicationJson, requestBody)

        then:
        1 * authentication.authenticate(_)
        1 * postProductValidatorV1.validatePostRequest(*_) >> requestVO
        1 * postProductServiceV1.doService(requestVO) >> responseVO
        1 * responseHandler.createResponse(responseVO) >> response
        0 * _
        responseEntity.body == requestBody
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.headers.get(HttpHeaders.ACCEPT) == [ MediaType.APPLICATION_JSON_VALUE ]
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test postProduct throws an error'() {

        setup:
        controller.authentication = authentication
        controller.postProductValidatorV1 = postProductValidatorV1
        controller.postProductServiceV1 = postProductServiceV1
        controller.responseHandler = responseHandler
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(requestBodyBad)
        RequestVO requestVO = new RequestVO(requestBodyBad, null, buildHeaders().toSingleValueMap())
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST,"Invalid request body object not schema compliant")

        when:
        ResponseEntity<String> responseEntity = controller.postProduct(token, applicationJson, applicationJson, requestBodyBad)

        then:
        1 * authentication.authenticate(_)
        1 * postProductValidatorV1.validatePostRequest(*_) >> { throw new FailFastException(errorVO, requestVO) }
        0 * postProductServiceV1.doService(_) >> responseVO
        0 * responseHandler.createResponse(*_) >> _
        0 * _
        thrown(FailFastException)
    }

    HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add('token', '112233')
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        return httpHeaders
    }
}

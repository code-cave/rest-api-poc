package com.company.org.controller

import com.company.org.controller.handler.ResponseHandler
import com.company.org.error.ErrorVO
import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.security.Authentication
import com.company.org.service.PutProductServiceV1
import com.company.org.validation.PutProductValidatorV1
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class PutProductControllerV1Spec extends Specification {

    PutProductControllerV1 controller = new PutProductControllerV1()
    Authentication authentication = Mock()
    PutProductValidatorV1 putProductValidatorV1 = Mock()
    PutProductServiceV1 putProductServiceV1 = Mock()
    ResponseHandler responseHandler = Mock()
    String applicationJson = MediaType.APPLICATION_JSON_VALUE
    String requestBody = '''{
        "_id": 123,
        "name": "Some cool item",
        "current_price": {
            "value": 19.99,
            "currency_code": "USD"
        }
    }'''
    String responseBody = '''{
        "_id": 123,
        "name": "Some cool item",
        "current_price": {
            "value": 13.99,
            "currency_code": "USD"
        }
    }'''

    def 'Test putProductById returns a result'() {

        setup:
        controller.authentication = authentication
        controller.putProductValidatorV1 = putProductValidatorV1
        controller.putProductServiceV1 = putProductServiceV1
        controller.responseHandler = responseHandler
        String id = '123'
        Map<String, String> pathVars = [ 'id': id ]
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(responseBody)
        RequestVO requestVO = new RequestVO(pathVars, buildHeaders().toSingleValueMap())
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), buildHeaders(), HttpStatus.OK)

        when:
        ResponseEntity<String> responseEntity = controller.putProductById(id, token, applicationJson, applicationJson, requestBody)

        then:
        1 * authentication.authenticate(_)
        1 * putProductValidatorV1.validatePutRequest(*_) >> requestVO
        1 * putProductServiceV1.doService(requestVO) >> responseVO
        1 * responseHandler.createResponse(responseVO) >> response
        0 * _
        responseEntity.body == responseBody
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.headers.get(HttpHeaders.ACCEPT) == [ MediaType.APPLICATION_JSON_VALUE ]
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test putProductById throws an error'() {

        setup:
        controller.authentication = authentication
        controller.putProductValidatorV1 = putProductValidatorV1
        controller.putProductServiceV1 = putProductServiceV1
        controller.responseHandler = responseHandler
        String id = 'OOPS'
        Map<String, String> pathVars = [ 'id': id ]
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(responseBody)
        RequestVO requestVO = new RequestVO(pathVars, buildHeaders().toSingleValueMap())
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST,"Invalid format for id path parameter")

        when:
        ResponseEntity<String> responseEntity = controller.putProductById(id, token, applicationJson, applicationJson, requestBody)

        then:
        1 * authentication.authenticate(_)
        1 * putProductValidatorV1.validatePutRequest(*_) >> { throw new FailFastException(errorVO, requestVO) }
        0 * putProductServiceV1.doService(_) >> responseVO
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

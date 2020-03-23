package com.company.org.controller

import com.company.org.controller.handler.ResponseHandler
import com.company.org.error.ErrorVO
import com.company.org.exception.FailFastException
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.security.Authentication
import com.company.org.service.GetProductsServiceV1
import com.company.org.validation.GetProductsValidatorV1
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class GetProductsControllerV1Spec extends Specification {

    GetProductsControllerV1 controller = new GetProductsControllerV1()
    Authentication authentication = Mock()
    GetProductsValidatorV1 getProductsValidatorV1 = Mock()
    GetProductsServiceV1 getProductsServiceV1 = Mock()
    ResponseHandler responseHandler = Mock()
    String applicationJson = MediaType.APPLICATION_JSON_VALUE
    String responseBody = '''{
        "products": [{
            "_id": 123,
            "name": "Some cool item",
            "current_price": {
                "value": 13.99,
                "currency_code": "USD"
            }
        }]
    }'''

    def 'Test getProductsById returns a result'() {

        setup:
        controller.authentication = authentication
        controller.getProductsValidatorV1 = getProductsValidatorV1
        controller.getProductsServiceV1 = getProductsServiceV1
        controller.responseHandler = responseHandler
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(responseBody)
        RequestVO requestVO = new RequestVO([:], buildHeaders().toSingleValueMap())
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), buildHeaders(), HttpStatus.OK)

        when:
        ResponseEntity<String> responseEntity = controller.getProducts(token, applicationJson)

        then:
        1 * authentication.authenticate(_)
        1 * getProductsValidatorV1.validateGetRequest(*_) >> requestVO
        1 * getProductsServiceV1.doService(requestVO) >> responseVO
        1 * responseHandler.createResponse(responseVO) >> response
        0 * _
        responseEntity.body == responseBody
        responseEntity.statusCode == HttpStatus.OK
        responseEntity.headers.get(HttpHeaders.ACCEPT) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test getProductsById throws an error'() {

        setup:
        controller.authentication = authentication
        controller.getProductsValidatorV1 = getProductsValidatorV1
        controller.getProductsServiceV1 = getProductsServiceV1
        controller.responseHandler = responseHandler
        String token = '112233'
        ResponseVO responseVO = new ResponseVO(responseBody)
        RequestVO requestVO = new RequestVO([:], buildHeaders().toSingleValueMap())
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST,"Invalid format for id path parameter")

        when:
        ResponseEntity<String> responseEntity = controller.getProducts(token, applicationJson)

        then:
        1 * authentication.authenticate(_)
        1 * getProductsValidatorV1.validateGetRequest(*_) >> { throw new FailFastException(errorVO, requestVO) }
        0 * getProductsServiceV1.doService(_) >> responseVO
        0 * responseHandler.createResponse(*_) >> _
        0 * _
        thrown(FailFastException)
    }

    HttpHeaders buildHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders()
        httpHeaders.add('token', '112233')
        httpHeaders.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        return httpHeaders
    }
}

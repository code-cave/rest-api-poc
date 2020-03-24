package com.company.org.controller.handler

import com.company.org.error.ErrorVO
import com.company.org.model.ResponseVO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import spock.lang.Specification

class ResponseHandlerSpec extends Specification {

    def 'Test createResponse'() {

        setup:
        ResponseHandler responseHandler = new ResponseHandler()
        ResponseVO responseVO = new ResponseVO('yay')

        when:
        ResponseEntity<String> responseEntity = responseHandler.createResponse(responseVO)

        then:
        responseEntity.getBody() == 'yay'
        responseEntity.getStatusCode() == HttpStatus.OK
        responseEntity.getHeaders() == HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS
    }

    def 'Test createErrorResponse'() {

        setup:
        ResponseHandler responseHandler = new ResponseHandler()
        ErrorVO errorVO = new ErrorVO(HttpStatus.NOT_FOUND, 'did not find it')

        when:
        ResponseEntity<String> responseEntity = responseHandler.createErrorResponse(errorVO)

        then:
        responseEntity.getBody() != null
        responseEntity.getStatusCode() == HttpStatus.NOT_FOUND
        responseEntity.getHeaders() == HttpHeaderHandler.DEFAULT_RESPONSE_HEADERS
    }
}

package com.company.org.exception

import com.company.org.controller.handler.ResponseHandler
import com.company.org.error.ErrorVO
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.model.avro.error.ErrorResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

class GlobalExceptionHandlerSpec extends Specification {

    GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler()
    ResponseHandler responseHandler = Mock(ResponseHandler)
    HttpHeaders headers = headers()
    HttpHeaders xmlHeaders = xmlHeaders()
    String requestBody = '''{
        "_id": 123,
        "name": "Some cool item",
        "current_price": {
            "value": 13.99,
            "currency_code": "USD"
        }
    }'''

    def 'Test logRequestBody POST'() {

        when:
        globalExceptionHandler.logRequestBody(500, 'error msg text', requestBody, HttpMethod.POST.name())

        then: true
    }

    def 'Test logRequestBody PUT'() {

        when:
        globalExceptionHandler.logRequestBody(500, 'error msg text', requestBody, HttpMethod.PUT.name())

        then: true
    }

    def 'Test logRequestBody null request body'() {

        when:
        globalExceptionHandler.logRequestBody(500, 'error msg text', null, HttpMethod.POST.name())

        then: true
    }

    def 'Test logRequestBody non 500'() {

        when:
        globalExceptionHandler.logRequestBody(404, 'error msg text', requestBody, HttpMethod.POST.name())

        then: true
    }

    def 'Test logRequestBody non 500 and null requestBody'() {

        when:
        globalExceptionHandler.logRequestBody(404, 'error msg text', null, HttpMethod.POST.name())

        then: true
    }

    def 'Test handleDataInputException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        String errorResponse = new ErrorResponse('some date', 400, 'Bad Request', 'bad request body')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        RequestVO requestVO = new RequestVO(requestBody, null, headers.toSingleValueMap())
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), headers, HttpStatus.BAD_REQUEST)
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST, 'bad request body')
        DataInputException dataInputException = new DataInputException(errorVO, requestVO)

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDataInputException(dataInputException)

        then:
        1 * responseHandler.createErrorResponse(dataInputException.getErrorVO()) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test handleAuthException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.ACCEPT) >> MediaType.APPLICATION_JSON_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 401, 'Unauthorized', 'bad user')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), headers, HttpStatus.UNAUTHORIZED)
        AuthException authException = new AuthException(HttpStatus.UNAUTHORIZED, 'bad user')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleAuthException(authException, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(authException.getErrorVO()) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.UNAUTHORIZED
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test handleServiceLayerException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.ACCEPT) >> MediaType.APPLICATION_JSON_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 400, 'Bad Request', 'bad input')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), headers, HttpStatus.BAD_REQUEST)
        ServiceLayerException serviceLayerException = new ServiceLayerException(HttpStatus.BAD_REQUEST, 'bad input')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleServiceLayerException(serviceLayerException, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(serviceLayerException.getErrorVO()) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test handleDataLayerException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.ACCEPT) >> MediaType.APPLICATION_JSON_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 404, 'Not Found', 'no data')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), headers, HttpStatus.NOT_FOUND)
        DataLayerException dataLayerException = new DataLayerException(HttpStatus.NOT_FOUND, 'no data')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleDataLayerException(dataLayerException, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(dataLayerException.getErrorVO()) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.NOT_FOUND
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    def 'Test httpMediaTypeNotAcceptableException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.ACCEPT) >> MediaType.APPLICATION_XML_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 406, 'Not Acceptable', 'not acceptable')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), xmlHeaders, HttpStatus.NOT_ACCEPTABLE)
        HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException = new HttpMediaTypeNotAcceptableException('not acceptable')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleHttpMediaTypeNotAcceptableException(httpMediaTypeNotAcceptableException, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(_) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.NOT_ACCEPTABLE
        responseEntity.headers.get(HttpHeaders.ACCEPT) == [ MediaType.APPLICATION_XML_VALUE ]
    }

    def 'Test httpMediaTypeNotSupportedException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.CONTENT_TYPE) >> MediaType.APPLICATION_XML_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 415, 'Not Supported', 'not acceptable')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), xmlHeaders, HttpStatus.HTTP_VERSION_NOT_SUPPORTED)
        HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = new HttpMediaTypeNotSupportedException('not supported')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleHttpMediaTypeNotSupportedException(httpMediaTypeNotSupportedException, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(_) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.HTTP_VERSION_NOT_SUPPORTED
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_XML_VALUE ]
    }

    def 'Test handleException'() {

        setup:
        globalExceptionHandler.responseHandler = responseHandler

        HttpServletRequest httpServletRequest = Stub(HttpServletRequest.class)
        httpServletRequest.getHeader(HttpHeaders.ACCEPT) >> MediaType.APPLICATION_JSON_VALUE
        httpServletRequest.getRequestURI() >> '/some/path'
        httpServletRequest.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE) >> requestBody
        String errorResponse = new ErrorResponse('some date', 400, 'Not Found', 'Error processing request')
        ResponseVO responseVO = new ResponseVO(errorResponse)
        ResponseEntity response = new ResponseEntity<>(responseVO.getResponseStr(), headers, HttpStatus.BAD_REQUEST)
        Exception exception = new Exception('oh snap')

        when:
        ResponseEntity<String> responseEntity = globalExceptionHandler.handleException(exception, httpServletRequest)

        then:
        1 * responseHandler.createErrorResponse(_) >> response
        0 * _
        responseEntity.body == errorResponse
        responseEntity.statusCode == HttpStatus.BAD_REQUEST
        responseEntity.headers.get(HttpHeaders.CONTENT_TYPE) == [ MediaType.APPLICATION_JSON_VALUE ]
    }

    HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        return headers;
    }

    HttpHeaders xmlHeaders() {
        HttpHeaders headers = new HttpHeaders()
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_XML_VALUE)
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
        return headers;
    }
}

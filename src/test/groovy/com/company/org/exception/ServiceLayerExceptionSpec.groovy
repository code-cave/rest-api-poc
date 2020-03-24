package com.company.org.exception

import com.company.org.model.RequestVO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class ServiceLayerExceptionSpec extends Specification {

    Map<String, String> pathVars = [ 'id': '123' ]
    Map<String, String> inputHeaders = [ (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test ServiceLayerException constructor 1'() {

        when:
        ServiceLayerException serviceLayerException = new ServiceLayerException(httpStatus, message)

        then:
        serviceLayerException.getStatusCode() == statusCode
        serviceLayerException.getMessage() == errorMessage
        serviceLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test ServiceLayerException constructor 2'() {

        when:
        ServiceLayerException serviceLayerException = new ServiceLayerException(httpStatus, message, new Throwable('foo'))

        then:
        serviceLayerException.getStatusCode() == statusCode
        serviceLayerException.getMessage() == errorMessage
        serviceLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test ServiceLayerException constructor 3'() {

        given:
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders)

        when:
        ServiceLayerException serviceLayerException = new ServiceLayerException(httpStatus, message, requestVO)

        then:
        serviceLayerException.getStatusCode() == statusCode
        serviceLayerException.getMessage() == errorMessage
        serviceLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test ServiceLayerException constructor 4'() {

        given:
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders)

        when:
        ServiceLayerException serviceLayerException = new ServiceLayerException(httpStatus, message, new Throwable('foo'), requestVO)

        then:
        serviceLayerException.getStatusCode() == statusCode
        serviceLayerException.getMessage() == errorMessage
        serviceLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test getters'() {

        setup:
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders)

        when:
        ServiceLayerException serviceLayerException = new ServiceLayerException(httpStatus, message, requestVO)

        then:
        serviceLayerException.getResponseContentType() == contentType

        where:
        httpStatus              | message       | statusCode                      | contentType
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | MediaType.APPLICATION_JSON
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | MediaType.APPLICATION_JSON
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | MediaType.APPLICATION_JSON
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | MediaType.APPLICATION_JSON
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | MediaType.APPLICATION_JSON
    }
}

package com.company.org.exception

import com.company.org.error.ErrorVO
import com.company.org.model.RequestVO
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class FailFastExceptionSpec extends Specification {

    Map<String, String> pathVars = [ 'id': '123' ]
    Map<String, String> inputHeaders = [ (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test FailFastException constructor 1'() {

        given:
        ErrorVO errorVO = new ErrorVO(httpStatus, message)
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders)

        when:
        FailFastException failFastException = new FailFastException(errorVO, requestVO)

        then:
        failFastException.getStatusCode() == statusCode
        failFastException.getMessage() == errorMessage
        failFastException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test FailFastException constructor 2'() {

        given:
        ErrorVO errorVO = new ErrorVO(httpStatus, message)
        RequestVO requestVO = new RequestVO(pathVars, inputHeaders)

        when:
        FailFastException failFastException = new FailFastException(errorVO, requestVO, new Throwable('foo'))

        then:
        failFastException.getStatusCode() == statusCode
        failFastException.getMessage() == errorMessage
        failFastException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }
}

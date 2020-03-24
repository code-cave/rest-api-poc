package com.company.org.exception

import org.springframework.http.HttpStatus
import spock.lang.Specification

class DataLayerExceptionSpec extends Specification {

    def 'Test DataLayerException constructor 1'() {

        when:
        DataLayerException dataLayerException = new DataLayerException(httpStatus, message)

        then:
        dataLayerException.getStatusCode() == statusCode
        dataLayerException.getMessage() == errorMessage
        dataLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test DataLayerException constructor 2'() {

        when:
        DataLayerException dataLayerException = new DataLayerException(httpStatus, message, new Throwable('foo'))

        then:
        dataLayerException.getStatusCode() == statusCode
        dataLayerException.getMessage() == errorMessage
        dataLayerException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }
}

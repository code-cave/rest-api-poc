package com.company.org.exception

import com.company.org.error.ErrorVO
import org.springframework.http.HttpStatus
import spock.lang.Specification

class BaseRuntimeExceptionSpec extends Specification {

    def 'Test BaseRuntimeException constructor 1'() {

        given:
        ErrorVO errorVO = new ErrorVO(httpStatus, message)

        when:
        BaseRuntimeException baseRuntimeException = new BaseRuntimeException(errorVO)

        then:
        baseRuntimeException.getStatusCode() == statusCode
        baseRuntimeException.getMessage() == errorMessage
        baseRuntimeException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test BaseRuntimeException constructor 2'() {

        given:
        ErrorVO errorVO = new ErrorVO(httpStatus, message)

        when:
        BaseRuntimeException baseRuntimeException = new BaseRuntimeException(errorVO, new Throwable('foo'))

        then:
        baseRuntimeException.getStatusCode() == statusCode
        baseRuntimeException.getMessage() == errorMessage
        baseRuntimeException.getErrorMessage() == builtErrorMessage

        where:
        httpStatus              | message       | statusCode                      | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST.value()  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED.value() | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND.value()    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT.value()     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR.value() | 'error' | '(Internal Server Error): error'
    }

    def 'Test BaseRuntimeException getters'() {

        setup:
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST, 'bad request')

        when:
        BaseRuntimeException baseRuntimeException = new BaseRuntimeException(errorVO)

        then:
        baseRuntimeException.getErrorVO() == errorVO
    }
}

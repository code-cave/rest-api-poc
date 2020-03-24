package com.company.org.error

import org.springframework.http.HttpStatus
import spock.lang.Specification

class ErrorVOSpec extends Specification {

    def 'Test constants'() {

        when:
        ErrorVO errorVO = new ErrorVO(HttpStatus.NOT_FOUND, 'oops')

        then:
        errorVO.serialVersionUID == 1
    }

    def 'Test ErrorVO'() {

        when:
        ErrorVO errorVO = new ErrorVO(status, message)

        then:
        errorVO.getHttpStatus() == httpStatus
        errorVO.getErrorMessage() == errorMessage
        errorVO.buildErrorMessage() == builtErrorMessage

        where:
        status                  | message       | httpStatus              | errorMessage  | builtErrorMessage
        HttpStatus.BAD_REQUEST  | 'bad request' | HttpStatus.BAD_REQUEST  | 'bad request' | '(Bad Request): bad request'
        HttpStatus.UNAUTHORIZED | 'bad user'    | HttpStatus.UNAUTHORIZED | 'bad user'    | '(Unauthorized): bad user'
        HttpStatus.NOT_FOUND    | 'not found'   | HttpStatus.NOT_FOUND    | 'not found'   | '(Not Found): not found'
        HttpStatus.CONFLICT     | 'bad stuff'   | HttpStatus.CONFLICT     | 'bad stuff'   | '(Conflict): bad stuff'
        HttpStatus.INTERNAL_SERVER_ERROR | 'error' | HttpStatus.INTERNAL_SERVER_ERROR | 'error' | '(Internal Server Error): error'
    }

    def 'Test getters and setters'() {

        setup:
        ErrorVO errorVO = new ErrorVO(HttpStatus.NOT_FOUND, 'oops')

        when:
        errorVO.setHttpStatus(HttpStatus.BAD_REQUEST)
        errorVO.setErrorMessage('bad request')

        then:
        errorVO.getHttpStatus() == HttpStatus.BAD_REQUEST
        errorVO.getErrorMessage() == 'bad request'
    }
}

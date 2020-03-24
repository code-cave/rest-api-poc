package com.company.org.security

import com.company.org.exception.AuthException
import spock.lang.Specification

class AuthenticationSpec extends Specification {

    def 'Test Authentication success'() {

        setup:
        Authentication authentication = new Authentication()

        when:
        authentication.authenticate('112233')

        then:
        notThrown(AuthException)
    }

    def 'Test Authentication fail case 1'() {

        setup:
        Authentication authentication = new Authentication()

        when:
        authentication.authenticate('')

        then:
        thrown(AuthException)
    }

    def 'Test Authentication fail case 2'() {

        setup:
        Authentication authentication = new Authentication()

        when:
        authentication.authenticate('yikes')

        then:
        thrown(AuthException)
    }
}

package com.company.org

import spock.lang.Specification

class ApplicationSpec extends Specification {

    def 'Test Application'() {

        setup:
        Application deal = new Application()

        when:
        deal.main(null)

        then:
        thrown IllegalArgumentException
    }
}

package com.company.org.dao.mongo

import spock.lang.Specification

class MongoDatabaseSpec extends Specification {

    def 'Test mongoOperations'() {

        setup:
        MongoDatabase mongoDatabase = Mock()

        when:
        mongoDatabase.mongoOperations()

        then:
        mongoDatabase != null
    }
}

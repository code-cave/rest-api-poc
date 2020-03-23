package com.company.org.service

import com.company.org.exception.ServiceLayerException
import com.company.org.model.RequestVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class DeleteProductServiceV1Spec extends Specification {

    MongoOperations mongoOperations = Mock()
    long id = 11223344
    Query query = new Query(new Criteria().where("_id").is(id))
    CurrentPrice currentPrice = new CurrentPrice(120.99, 'USD')
    Product product = new Product(id, 'foo', currentPrice)

    Map headers = [ (HttpHeaders.ACCEPT): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test doService success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        DeleteProductServiceV1 service = new DeleteProductServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findAndRemove(query, Product.class, 'products') >> product
        0 * _
    }

    def 'Test doService fail'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        DeleteProductServiceV1 service = new DeleteProductServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findAndRemove(query, Product.class, 'products') >> null
        0 * _
        thrown(ServiceLayerException)
    }

    def 'Test checkResult success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        DeleteProductServiceV1 service = new DeleteProductServiceV1(mongoOperations)

        when:
        service.checkResult(product, requestVO)

        then:
        product != null
    }

    def 'Test checkResult fail'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        DeleteProductServiceV1 service = new DeleteProductServiceV1(mongoOperations)

        when:
        service.checkResult(null, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Cannot delete due to product record with id 11223344 not found'
    }
}

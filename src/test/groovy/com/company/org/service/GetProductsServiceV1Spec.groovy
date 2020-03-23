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

class GetProductsServiceV1Spec extends Specification {

    MongoOperations mongoOperations = Mock()
    long id = 11223344
    CurrentPrice currentPrice = new CurrentPrice(120.99, 'USD')
    Product product = new Product(id, 'foo', currentPrice)
    List<Product> products = [ product ]

    Map headers = [ (HttpHeaders.ACCEPT): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test doService success'() {

        setup:
        RequestVO requestVO = new RequestVO([:], headers)
        GetProductsServiceV1 service = new GetProductsServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findAll(Product.class, 'products') >> products
        0 * _
    }

    def 'Test doService fail'() {

        setup:
        RequestVO requestVO = new RequestVO([:], headers)
        GetProductsServiceV1 service = new GetProductsServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findAll(Product.class, 'products') >> null
        0 * _
        thrown(ServiceLayerException)
    }

    def 'Test checkResult success'() {

        setup:
        RequestVO requestVO = new RequestVO([:], headers)
        GetProductsServiceV1 service = new GetProductsServiceV1(mongoOperations)

        when:
        service.checkResult(products, requestVO)

        then:
        product != null
    }

    def 'Test checkResult fail case 1'() {

        setup:
        RequestVO requestVO = new RequestVO([:], headers)
        GetProductsServiceV1 service = new GetProductsServiceV1(mongoOperations)

        when:
        service.checkResult(null, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'No Product records found in database'
    }

    def 'Test checkResult fail case 2'() {

        setup:
        RequestVO requestVO = new RequestVO([:], headers)
        GetProductsServiceV1 service = new GetProductsServiceV1(mongoOperations)

        when:
        service.checkResult([], requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'No Product records found in database'
    }
}

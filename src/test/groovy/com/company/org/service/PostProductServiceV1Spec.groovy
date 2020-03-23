package com.company.org.service

import com.company.org.exception.ServiceLayerException
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class PostProductServiceV1Spec extends Specification {

    MongoOperations mongoOperations = Mock()
    long id = 11223344

    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]

    def 'Test doService success'() {

        setup:
        Product product = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(product.toString(), null, headers)
        requestVO.setAvroObject(product)
        PostProductServiceV1 service = new PostProductServiceV1(mongoOperations)

        when:
        ResponseVO responseVO = service.doService(requestVO)

        then:
        1 * mongoOperations.save(_, 'products') >> product
        0 * _
        responseVO.getResponseStr() == product.toString()
    }

    def 'Test doService fail case 1'() {

        setup:
        Product product = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(product.toString(), null, headers)
        requestVO.setAvroObject(product)
        PostProductServiceV1 service = new PostProductServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.save(_, 'products') >> null
        0 * _
        thrown(ServiceLayerException)
    }

    def 'Test checkResult success'() {

        setup:
        Product product = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(product.toString(), null, headers)
        requestVO.setAvroObject(product)
        PostProductServiceV1 service = new PostProductServiceV1(mongoOperations)

        when:
        service.checkResult(product, requestVO)

        then:
        product != null
    }

    def 'Test checkResult fail'() {

        setup:
        Product product = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(product.toString(), null, headers)
        requestVO.setAvroObject(product)
        PostProductServiceV1 service = new PostProductServiceV1(mongoOperations)

        when:
        service.checkResult(null, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.CONFLICT.value()
        sle.getMessage() == 'Unable to insert product with id 11223344'
    }
}

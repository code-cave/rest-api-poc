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
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponents
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

class GetProductAlteredNameServiceV1Spec extends Specification {

    MongoOperations mongoOperations = Mock()
    RestTemplate template = Mock()
    long id = 11223344
    Query query = new Query(new Criteria().where("_id").is(id))
    CurrentPrice currentPrice = new CurrentPrice(120.99, 'USD')

    Map headers = [ (HttpHeaders.ACCEPT): MediaType.APPLICATION_JSON_VALUE ]

    def 'Test buildURI'() {

        when:
        UriComponents uriComponents = UriComponentsBuilder.newInstance()
            .scheme('https')
            .host('redsky.target.com')
            .path('/v2/pdp/tcin/{id}')
            .query('excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics')
            .build()

        then:
        uriComponents == GetProductAlteredNameServiceV1.URI
    }

    def 'Test doService success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "product": { "item": { "product_description": { "title": "foobar" } } } }'
        ResponseEntity<String> responseEntity = new ResponseEntity<>(externalNameRecord, HttpStatus.OK)
        service.template = template
        Product product = new Product(id, 'foo', currentPrice)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findOne(query, Product.class, 'products') >> product
        1 * template.exchange(_, String.class) >> responseEntity
        0 * _
        product.getName() == 'foobar'
    }

    def 'Test doService fail'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.findOne(query, Product.class, 'products') >> null
        0 * _
        thrown(ServiceLayerException)
    }

    def 'Test checkResult success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        Product product = new Product(id, 'foo', currentPrice)

        when:
        service.checkResult(product, requestVO)

        then:
        product != null
    }

    def 'Test checkResult fail'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)

        when:
        service.checkResult(null, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Product record with id 11223344 not found'
    }

    def 'Test searchAlteredName success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "product": { "item": { "product_description": { "title": "foobar" } } } }'
        ResponseEntity<String> responseEntity = new ResponseEntity<>(externalNameRecord, HttpStatus.OK)
        Product product = new Product(id, 'foo', currentPrice)
        service.template = template

        when:
        service.searchAlteredName(product, requestVO)

        then:
        1 * template.exchange(_, String.class) >> responseEntity
        0 * _
        product.getName() == 'foobar'
    }

    def 'Test searchAlteredName fail case 1'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        Product product = new Product(id, 'foo', currentPrice)
        service.template = template

        when:
        service.searchAlteredName(product, requestVO)

        then:
        1 * template.exchange(_, String.class) >> { throw new RestClientException("dummy") }
        0 * _
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Product altered name record with id 11223344 not found at external service'
    }

    def 'Test searchAlteredName fail case 2'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        Product product = new Product(id, 'foo', currentPrice)
        service.template = template

        when:
        service.searchAlteredName(product, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Product altered name record with id 11223344 not found at external service'
    }

    def 'Test checkAlteredName success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "product": { "item": { "product_description": { "title": "foobar" } } } }'
        ResponseEntity<String> responseEntity = new ResponseEntity<>(externalNameRecord, HttpStatus.OK)
        Product product = new Product(id, 'foo', currentPrice)

        when:
        String alteredName = service.checkAlteredName(requestVO, product, responseEntity)

        then:
        alteredName == 'foobar'
    }

    def 'Test checkAlteredName fail'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        ResponseEntity<String> responseEntity = new ResponseEntity<>('', HttpStatus.NOT_FOUND)
        Product product = new Product(id, 'foo', currentPrice)

        when:
        service.checkAlteredName(requestVO, product, responseEntity)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Product altered name record with id 11223344 not found at external service'
    }

    def 'Test extractAlteredName success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "product": { "item": { "product_description": { "title": "foobar" } } } }'

        when:
        String alteredName = service.extractAlteredName(requestVO, externalNameRecord)

        then:
        alteredName == 'foobar'
    }

    def 'Test extractAlteredName fail case 1'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "OOPS": { "item": { "product_description": { "title": "foobar" } } } }'

        when:
        service.extractAlteredName(requestVO, externalNameRecord)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.CONFLICT.value()
        sle.getMessage() == 'Altered name record found but is missing name or is not correct JSON object'
    }

    def 'Test extractAlteredName fail case 2'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        RequestVO requestVO = new RequestVO(pathVars, headers)
        GetProductAlteredNameServiceV1 service = new GetProductAlteredNameServiceV1(mongoOperations)
        String externalNameRecord = '{ "OOPS" }'

        when:
        service.extractAlteredName(requestVO, externalNameRecord)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.CONFLICT.value()
        sle.getMessage() == 'Altered name record is not valid JSON'
    }
}

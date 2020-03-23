package com.company.org.service

import com.company.org.exception.ServiceLayerException
import com.company.org.model.RequestVO
import com.company.org.model.ResponseVO
import com.company.org.model.avro.product.CurrentPrice
import com.company.org.model.avro.product.Product
import com.mongodb.client.result.UpdateResult
import org.bson.BsonValue
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import spock.lang.Specification

class PutProductServiceV1Spec extends Specification {

    MongoOperations mongoOperations = Mock()
    long id = 11223344

    Map headers = [
        (HttpHeaders.ACCEPT)      : MediaType.APPLICATION_JSON_VALUE,
        (HttpHeaders.CONTENT_TYPE): MediaType.APPLICATION_JSON_VALUE
    ]

    def 'Test doService success'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        Product updatedProduct = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(updatedProduct.toString(), pathVars, headers)
        requestVO.setAvroObject(updatedProduct)
        PutProductServiceV1 service = new PutProductServiceV1(mongoOperations)
        UpdateResult updateResult = newUpdateResult(true, 1, true, 1, null)

        when:
        ResponseVO responseVO = service.doService(requestVO)

        then:
        1 * mongoOperations.updateFirst(_, _, Product.class, 'products') >> updateResult
        0 * _
        responseVO.getResponseStr() == updatedProduct.toString()
    }

    def 'Test doService fail case 1'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        Product updatedProduct = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(updatedProduct.toString(), pathVars, headers)
        requestVO.setAvroObject(updatedProduct)
        PutProductServiceV1 service = new PutProductServiceV1(mongoOperations)
        UpdateResult updateResult = newUpdateResult(true, 0, true, 0, null)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.updateFirst(_, _, Product.class, 'products') >> updateResult
        0 * _
        thrown(ServiceLayerException)
    }

    def 'Test doService fail case 2'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        Product updatedProduct = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(updatedProduct.toString(), pathVars, headers)
        requestVO.setAvroObject(updatedProduct)
        PutProductServiceV1 service = new PutProductServiceV1(mongoOperations)
        UpdateResult updateResult = newUpdateResult(true, 1, false, 0, null)

        when:
        service.doService(requestVO)

        then:
        1 * mongoOperations.updateFirst(_, _, Product.class, 'products') >> updateResult
        0 * _
        thrown(ServiceLayerException)
    }


    def 'Test checkResult fail case 1'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        Product updatedProduct = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(updatedProduct.toString(), pathVars, headers)
        requestVO.setAvroObject(updatedProduct)
        PutProductServiceV1 service = new PutProductServiceV1(mongoOperations)
        UpdateResult updateResult = newUpdateResult(true, 1, false, 0, null)

        when:
        service.checkResult(updateResult, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Unable to update, product with id 11223344 not found'
    }

    def 'Test checkResult fail case 2'() {

        setup:
        Map pathVars = [ 'id': '11223344' ]
        Product updatedProduct = new Product(id, 'foo', new CurrentPrice(18.99, 'USD'))
        RequestVO requestVO = new RequestVO(updatedProduct.toString(), pathVars, headers)
        requestVO.setAvroObject(updatedProduct)
        PutProductServiceV1 service = new PutProductServiceV1(mongoOperations)
        UpdateResult updateResult = newUpdateResult(true, 0, true, 0, null)

        when:
        service.checkResult(updateResult, requestVO)

        then:
        ServiceLayerException sle = thrown(ServiceLayerException)
        sle.getStatusCode() == HttpStatus.NOT_FOUND.value()
        sle.getMessage() == 'Unable to update, product with id 11223344 not found'
    }

    // Making this method to save space and make life easier making these
    UpdateResult newUpdateResult(boolean ack, int matchedCount, boolean countAvail, int modifiedCount, BsonValue id) {
        return new UpdateResult() {
            @Override
            boolean wasAcknowledged() {
                return ack
            }
            @Override
            long getMatchedCount() {
                return matchedCount
            }
            @Override
            boolean isModifiedCountAvailable() {
                return countAvail
            }
            @Override
            long getModifiedCount() {
                return modifiedCount
            }
            @Override
            BsonValue getUpsertedId() {
                return id
            }
        }
    }
}

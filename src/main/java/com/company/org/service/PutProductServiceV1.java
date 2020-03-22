package com.company.org.service;

import com.company.org.exception.ServiceLayerException;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.product.Product;
import com.mongodb.client.result.UpdateResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PutProductServiceV1 {

    @Autowired
    private MongoOperations mongoOperations;

    public PutProductServiceV1(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseVO doService(RequestVO requestVO) {

        Product product = updateMongo(requestVO);

        return new ResponseVO(product.toString());
    }

    private Product updateMongo(RequestVO requestVO) {

        Product product = (Product)requestVO.getAvroObject();
        Double newCurrentPriceValue = product.getCurrentPrice().getValue();
        Long id = Long.parseLong(requestVO.getPathVars().get("id"));
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = Update.update("current_price.value", newCurrentPriceValue);
        checkResult(mongoOperations.updateFirst(query, update, Product.class, "products"), requestVO);
        return product;
    }

    private void checkResult(UpdateResult updateResult, RequestVO requestVO) {
        // If the update was successful, the modified count will be 1
        // The modified count can never be greater than 1 because _id is unique
        // If inserting with same price, then the getMatchedCount flag will pass through
        if (!updateResult.isModifiedCountAvailable() || updateResult.getMatchedCount() != 1) {
            String message = "Unable to update, product with id " + requestVO.getPathVars().get("id") + " not found";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }
    }
}
package com.company.org.service;

import com.company.org.exception.ServiceLayerException;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class GetProductServiceV1 {

    @Autowired
    private MongoOperations mongoOperations;

    public GetProductServiceV1(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseVO doService(RequestVO requestVO) {

        Product product = searchMongo(requestVO);
        checkResult(product, requestVO);
        // Return the Avro object serialized to a string
        return new ResponseVO(product.toString());
    }

    private Product searchMongo(RequestVO requestVO) {
        // Search the database based on the unique _id value
        Long id = Long.parseLong(requestVO.getPathVars().get("id"));
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoOperations.findOne(query, Product.class, "products");
    }

    private void checkResult(Product product, RequestVO requestVO) {
        // If not found, product will be null
        if (product == null) {
            String message = "Product record with id " + requestVO.getPathVars().get("id") + " not found";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }
    }
}
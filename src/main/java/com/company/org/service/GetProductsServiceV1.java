package com.company.org.service;

import com.company.org.exception.ServiceLayerException;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.product.Product;
import com.company.org.model.avro.product.Products;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetProductsServiceV1 {

    @Autowired
    private MongoOperations mongoOperations;

    public GetProductsServiceV1(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseVO doService(RequestVO requestVO) {

        List<Product> products = searchMongo(requestVO);
        checkResult(products, requestVO);
        // Return the Avro object serialized to a string
        return new ResponseVO(new Products(products).toString());
    }

    private List<Product> searchMongo(RequestVO requestVO) {
        // Search the database for all of the product records
        // keeping requestVO here in case needed later
        return mongoOperations.findAll(Product.class, "products");
    }

    private void checkResult(List<Product> products, RequestVO requestVO) {
        // If not found, product will be null
        if (products == null || products.size() == 0) {
            String message = "No Product records found in database";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }
    }
}
package com.company.org.service;

import com.company.org.exception.ServiceLayerException;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class PostProductServiceV1 {

    @Autowired
    private MongoOperations mongoOperations;

    public PostProductServiceV1(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseVO doService(RequestVO requestVO) {

        Product product = saveMongo(requestVO);

        return new ResponseVO(product.toString());
    }

    private Product saveMongo(RequestVO requestVO) {

        Product product = (Product)requestVO.getAvroObject();
        return checkResult(mongoOperations.save(product, "products"), requestVO);
    }

    private Product checkResult(Product product, RequestVO requestVO) {
        // If the insert is successful, the product will not be null
        if (product == null) {
            String message = "Unable to insert product with id " + ((Product)requestVO.getAvroObject()).getId$1();
            throw new ServiceLayerException(HttpStatus.CONFLICT, message, requestVO);
        }
        return product;
    }
}
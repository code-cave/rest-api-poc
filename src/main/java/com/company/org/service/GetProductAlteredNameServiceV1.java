package com.company.org.service;

import com.company.org.exception.ServiceLayerException;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.avro.product.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@Component
public class GetProductAlteredNameServiceV1 {

    private static final RestTemplate TEMPLATE = new RestTemplate();
    private static final UriComponents URI = buildURI();
    private static final Gson GSON = new Gson();

    @Autowired
    private MongoOperations mongoOperations;

    public GetProductAlteredNameServiceV1(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    public ResponseVO doService(RequestVO requestVO) {

        Product product = searchMongo(requestVO);
        checkResult(product, requestVO);
        product = searchAlteredName(product, requestVO);
        // Return the Avro object serialized to a string
        return new ResponseVO(product.toString());
    }

    private Product searchMongo(RequestVO requestVO) {
        // Search the database based on the unique _id value
        Long id = Long.parseLong(requestVO.getPathVars().get("id"));
        Query query = new Query(Criteria.where("_id").is(id));
        return mongoOperations.findOne(query, Product.class, "products");
    }

    private Product searchAlteredName(Product product, RequestVO requestVO) {
        // Keeping requestVO here in case it is needed later
        // Search for the different product name value
        URI uri = URI.expand(Collections.singletonMap("id", String.valueOf(product.getId$1()))).encode().toUri();
        ResponseEntity<String> response;
        // Gotta catch those nasty errors
        try {
            response = TEMPLATE.exchange(new RequestEntity<String>(HttpMethod.GET, uri), String.class);
        }
        catch (RestClientException e) {
            String message = "Product altered name record with id " +
                product.getId$1() + " not found at external service";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }

        product.setName(checkAlteredName(requestVO, product, response));
        return product;
    }

    private void checkResult(Product product, RequestVO requestVO) {
        // If not found, product will be null
        if (product == null) {
            String message = "Product record with id " + requestVO.getPathVars().get("id") + " not found";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }
    }

    private String checkAlteredName(RequestVO requestVO, Product product, ResponseEntity<String> response) {

        if (response.getStatusCode() != HttpStatus.OK) {
            String message = "Product altered name record with id " +
                product.getId$1() + " not found at external service";
            throw new ServiceLayerException(HttpStatus.NOT_FOUND, message, requestVO);
        }

        return extractAlteredName(requestVO, response.getBody());
    }

    private String extractAlteredName(RequestVO requestVO, String responseBody) {

        try {
            JsonObject json = GSON.fromJson(responseBody, JsonObject.class);
            return json.getAsJsonObject("product")
                .getAsJsonObject("item")
                .getAsJsonObject("product_description")
                .get("title")
                .getAsString();
        }
        catch (JsonSyntaxException e) {
            String message = "Altered name record is not valid JSON";
            throw new ServiceLayerException(HttpStatus.CONFLICT, message, requestVO);
        }
        catch (Exception e) {
            String message = "Altered name record found but is missing name or is not correct JSON object";
            throw new ServiceLayerException(HttpStatus.CONFLICT, message, requestVO);
        }
    }
    private static UriComponents buildURI() {

        String queryParam = "excludes=taxonomy,price,promotion,bulk_ship,rating_and_review_reviews,rating_and_review_statistics,question_answer_statistics";

        return UriComponentsBuilder.newInstance()
            .scheme("https")
            .host("redsky.target.com")
            .path("/v2/pdp/tcin/{id}")
            .query(queryParam)
            .build();
    }
}
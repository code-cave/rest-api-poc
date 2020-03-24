package com.company.org.validation;

import com.company.org.model.RequestVO;
import com.company.org.model.avro.product.Product;
import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PostProductValidatorV1 implements RequestValidatorBase {

    private static final String INVALID_REQUEST_BODY = "Invalid request body object not schema compliant";

    private static final Schema PRODUCT_SCHEMA = Product.getClassSchema();
    private static final DatumReader<Product> READER = new SpecificDatumReader<>(PRODUCT_SCHEMA);

    @Override
    public void validateRequest(RequestVO requestVO) {
        // Make sure the request body is actually Avro schema compliant
        // The Content-Type and Accept headers are checked in the RequestValidatorBase
        try {
            Product product = validateRequestBody(requestVO);
            ValidationUtility.validateCurrencyValue(requestVO, product.getCurrentPrice().getValue());
        }
        catch (AvroTypeException | IOException e) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_REQUEST_BODY, requestVO);
        }
    }

    private Product validateRequestBody(RequestVO requestVO) throws AvroTypeException, IOException {
        // Turn the json string into an Avro Product object if possible
        Decoder decoder = DecoderFactory.get().jsonDecoder(PRODUCT_SCHEMA, requestVO.getInputReqBodyString());
        Product product = READER.read(null, decoder);
        requestVO.setAvroObject(product);
        return product;
    }
}

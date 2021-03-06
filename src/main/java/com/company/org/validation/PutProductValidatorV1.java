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
public class PutProductValidatorV1 implements RequestValidatorBase {

    private static final String INVALID_ID_PARAMETER = "Invalid format for id path parameter";
    private static final String INVALID_REQUEST_BODY = "Invalid request body object not schema compliant";
    private static final String INVALID_REQUEST_BODY_ID = "Invalid request body id does not match path id";

    private static final DatumReader<Product> READER = new SpecificDatumReader<>(Product.class);
    private static final DecoderFactory DECODER_FACTORY = DecoderFactory.get();
    private static final Schema PRODUCT_SCHEMA = Product.getClassSchema();

    @Override
    public void validateRequest(RequestVO requestVO) {
        // Just making sure to check that the id is actually of type long
        // and that the request body is actually Avro schema compliant
        // The Content-Type and Accept headers are checked in the RequestValidatorBase
        long id = 0;
        try {
            id = Long.parseLong(requestVO.getPathVars().get("id"));
            Product product = validateRequestBody(requestVO);
            ValidationUtility.validateCurrencyValue(requestVO, product.getCurrentPrice().getValue());
        }
        catch (NumberFormatException e) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_ID_PARAMETER, requestVO);
        }
        catch (AvroTypeException | IOException e) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_REQUEST_BODY, requestVO);
        }
        // Once we get here, we know the id and schema are correct types
        validateId(requestVO, id);
    }

    private Product validateRequestBody(RequestVO requestVO) throws AvroTypeException, IOException {
        // Turn the json string into an Avro Product object if possible
        Decoder decoder = DECODER_FACTORY.jsonDecoder(PRODUCT_SCHEMA, requestVO.getInputReqBodyString());
        Product product = READER.read(null, decoder);
        requestVO.setAvroObject(product);
        return product;
    }

    private void validateId(RequestVO requestVO, long id) {
        // Gotta make sure that the ids are the same
        Product product = (Product)requestVO.getAvroObject();
        if (id != product.getId$1()) {
            ValidationUtility.failFast(HttpStatus.BAD_REQUEST, INVALID_REQUEST_BODY_ID, requestVO);
        }
    }
}

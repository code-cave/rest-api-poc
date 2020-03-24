package com.company.org.controller;

import com.company.org.controller.handler.ResponseHandler;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.swagger.ErrorResponse;
import com.company.org.model.swagger.Product;
import com.company.org.security.Authentication;
import com.company.org.service.DeleteProductServiceV1;
import com.company.org.validation.DeleteProductValidatorV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DeleteProductControllerV1 {

    @Autowired
    Authentication authentication;

    @Autowired
    DeleteProductValidatorV1 deleteProductValidatorV1;

    @Autowired
    DeleteProductServiceV1 deleteProductServiceV1;

    @Autowired
    ResponseHandler responseHandler;

    @ResponseBody
    @DeleteMapping(
        path = "/retail/product/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Delete Product Record",
        description = "Performs a DELETE operation to delete a product record based on id",
        tags = "MyRetailAPI",
        parameters = {
            @Parameter(
                name = "id",
                in = ParameterIn.PATH,
                description = "Must be of type long",
                example = "13860428",
                required = true
            ),
            @Parameter(
                name = "token",
                in = ParameterIn.HEADER,
                description = "The authentication token",
                example = "112233",
                required = true
            ),
            @Parameter(
                name = "Accept",
                in = ParameterIn.HEADER,
                description = "The Accept MIME type",
                example = "application/json",
                required = true
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Success",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Product.class),
            examples = @ExampleObject(value = "{ \"_id\": 13860428, \"name\": \"The Big Lebowski (Blu-ray) (Widescreen)\", \"current_price\": { \"value\": 13.49, \"currency_code\": \"USD\" } }"))
        ),
        @ApiResponse(
            responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid format for id path parameter\" }"))
        ),
        @ApiResponse(
            responseCode = "401", description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Authentication failed\" }"))
        ),
        @ApiResponse(
            responseCode = "404", description = "Not Found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 404, \"error\": \"Not Found\", \"message\": \"Product record not found\" }"))
        ),
        @ApiResponse(
            responseCode = "406", description = "Not Acceptable",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 406, \"error\": \"Not Acceptable\", \"message\": \"Accept application/xml not supported\" }"))
        ),
        @ApiResponse(
            responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"An internal server error has occurred\" }"))
        )
    })
    public ResponseEntity<String> deleteProductById(@PathVariable(name = "id") String id,
                                                 @RequestHeader(name = "token") String token,
                                                 @RequestHeader(name = "Accept") String accept) {
        /*
         * The id is of type String so that validation can handle in the validator class
         * and throw the specific bad request error with the error message as opposed to
         * having the Spring frontend throw the error with its own message
         */
        // Deal with path variables
        Map<String, String> pathVars = new HashMap<>();
        pathVars.put("id", id);
        // Deal with headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        httpHeaders.add(HttpHeaders.ACCEPT, accept);

        authentication.authenticate(token);

        RequestVO requestVO = deleteProductValidatorV1.validateDeleteRequest(pathVars, httpHeaders.toSingleValueMap());

        ResponseVO response = deleteProductServiceV1.doService(requestVO);

        return responseHandler.createResponse(response);
    }
}

package com.company.org.controller;

import com.company.org.controller.handler.ResponseHandler;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.model.swagger.ErrorResponse;
import com.company.org.model.swagger.Product;
import com.company.org.security.Authentication;
import com.company.org.service.PostProductServiceV1;
import com.company.org.validation.PostProductValidatorV1;
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
public class PostProductControllerV1 {

    @Autowired
    Authentication authentication;

    @Autowired
    PostProductValidatorV1 postProductValidatorV1;

    @Autowired
    PostProductServiceV1 postProductServiceV1;

    @Autowired
    ResponseHandler responseHandler;

    @ResponseBody
    @PostMapping(
        path = "/retail/product",
        consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Post Product Record",
        description = "Performs a POST operation to insert product record price based on request body",
        tags = "MyRetailAPI",
        parameters = {
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
            ),
            @Parameter(
                name = "Content-Type",
                in = ParameterIn.HEADER,
                description = "The Content-Type MIME type",
                example = "application/json",
                required = true
            ),
        },
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "The request body for the request",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Product.class),
                examples = @ExampleObject(value = "{ \"_id\": 99887766, \"name\": \"GI Joe Action Figure\", \"current_price\": { \"value\": 10.99, \"currency_code\": \"USD\" } }")
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Success",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = Product.class),
            examples = @ExampleObject(value = "{ \"_id\": 99887766, \"name\": \"GI Joe Action Figure\", \"current_price\": { \"value\": 10.99, \"currency_code\": \"USD\" } }"))
        ),
        @ApiResponse(
            responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 400, \"error\": \"Bad Request\", \"message\": \"Invalid request body object\" }"))
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
            responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class),
            examples = @ExampleObject(value = "{ \"timeStamp\": \"Sat Mar 21 17:00:00 GMT 2020\", \"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"An internal server error has occurred\" }"))
        )
    })
    public ResponseEntity<String> postProduct(@RequestHeader(name = "token") String token,
                                              @RequestHeader(name = "Accept") String accept,
                                              @RequestHeader(name = "Content-Type") String contentType,
                                              @RequestBody String requestBody) {
        // Deal with headers
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("token", token);
        httpHeaders.add(HttpHeaders.ACCEPT, accept);
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);

        authentication.authenticate(token);

        RequestVO requestVO = postProductValidatorV1.validatePostRequest(requestBody, httpHeaders.toSingleValueMap());

        ResponseVO response = postProductServiceV1.doService(requestVO);

        return responseHandler.createResponse(response);
    }
}

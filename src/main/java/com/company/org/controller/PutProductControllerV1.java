package com.company.org.controller;

import com.company.org.controller.handler.ResponseHandler;
import com.company.org.model.ErrorResponse;
import com.company.org.model.ProductDataModel;
import com.company.org.model.RequestVO;
import com.company.org.model.ResponseVO;
import com.company.org.security.Authentication;
import com.company.org.validation.GetProductValidatorV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class PutProductControllerV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(PutProductControllerV1.class);

    @Autowired
    Authentication authentication;

    @Autowired
    GetProductValidatorV1 getProductValidatorV1;

    //@Autowired
    //PutProductServiceV1 putProductServiceV1;

    @Autowired
    ResponseHandler responseHandler;

    @ResponseBody
    @PutMapping(
        path = "/retail/product/{id}",
        consumes = { MediaType.APPLICATION_JSON_VALUE },
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Put Product Record",
        description = "Performs a Put operation to update product price based on id and request body",
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
            ),
            @Parameter(
                name = "Content-Type",
                in = ParameterIn.HEADER,
                description = "The Content-Type MIME type",
                example = "application/json",
                required = true
            )
        }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", description = "Success",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ProductDataModel.class))
        ),
        @ApiResponse(
            responseCode = "400", description = "Bad Request",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "401", description = "Unauthorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "404", description = "Not Found",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "500", description = "Internal Server Error",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    public ResponseEntity<String> putProductById(@PathVariable(name = "id") String id,
                                                 @RequestHeader(name = "token") String token,
                                                 @RequestHeader(name = "Accept") String accept,
                                                 @RequestHeader(name = "Content-Type") String contentType,
                                                 @RequestBody String requestBody) {
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
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, contentType);

        authentication.authenticate(token);

        RequestVO requestVO = getProductValidatorV1.validateGetRequest(pathVars, httpHeaders.toSingleValueMap());

        ResponseVO response = new ResponseVO("{ \"cool\": \"json\" }");

        return responseHandler.createResponse(response);
    }
}

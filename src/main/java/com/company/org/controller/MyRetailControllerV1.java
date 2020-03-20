package com.company.org.controller;

import com.company.org.controller.utility.ResponseHandler;
import com.company.org.model.ResponseVO;
import com.company.org.model.ErrorResponse;
import com.company.org.model.ProductDataModel;
import com.company.org.security.Authentication;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MyRetailControllerV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRetailControllerV1.class);

    @Autowired
    Authentication authentication;

    //@Autowired
    //FindMasterIdentifiersValidatorV1 findMasterIdentifiersValidatorV1;

    //@Autowired
    //FindMasterIdentifiersServiceV1 findMasterIdentifiersServiceV1;

    @Autowired
    ResponseHandler responseHandler;

    @ResponseBody
    @GetMapping(
        path = "/retail/products/{id}",
        produces = { MediaType.APPLICATION_JSON_VALUE }
    )
    @Operation(
        summary = "Get Product Record",
        description = "Performs a GET operation to retrieve products based on id",
        tags = "MyRetailAPI",
        parameters = {
            @Parameter(name = "id", required = true),
            @Parameter(name = "token", required = true, example = "112233")
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
    public ResponseEntity<String> getProductById(@PathVariable(value = "id") String id, @RequestHeader String token) {

        authentication.authenticate(token);

        ResponseVO response = new ResponseVO("{ \"cool\": \"json\" }");

        return responseHandler.createResponse(response);
    }
}

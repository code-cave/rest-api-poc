package com.company.org.controller;

import com.company.org.controller.utility.ResponseHandler;
import com.company.org.model.ResponseVO;
import com.company.org.security.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping("/retail/products/{id}")
    public ResponseEntity<String> getProductById(@PathVariable(value = "id") String productId,
                                                 @RequestHeader String token) {

        authentication.authenticate(token);

        ResponseVO response = new ResponseVO("{ \"cool\": \"json\" }");

        return responseHandler.createResponse(response);
    }
}

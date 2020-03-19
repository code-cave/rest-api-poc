package com.company.org.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class MyRetailControllerV1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRetailControllerV1.class);

    //@Autowired
    //FindMasterIdentifiersValidatorV1 findMasterIdentifiersValidatorV1;

    //@Autowired
    //FindMasterIdentifiersServiceV1 findMasterIdentifiersServiceV1;

    //@Autowired
    //ResponseHandler responseHandler;

    @ResponseBody
    @GetMapping("/retail/products/{id}")
    public ResponseEntity<String> getProductById(@PathVariable(value = "id") Long productId,
                                                 @RequestHeader String token) {

        return ResponseEntity.ok().body("{ \"cool\": \"json\" }");

    }
}

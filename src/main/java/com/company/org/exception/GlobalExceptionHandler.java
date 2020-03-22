package com.company.org.exception;

import com.company.org.Application;
import com.company.org.controller.handler.ResponseHandler;
import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private ResponseHandler responseHandler;

    @ExceptionHandler(value = {DataInputException.class})
    protected ResponseEntity<String> handleDataInputException(DataInputException dataInputException) {

        LOGGER.error(dataInputException.getErrorMessage());

        return responseHandler.createErrorResponse(dataInputException.getErrorVO());
    }

    @ExceptionHandler({AuthException.class})
    protected ResponseEntity<String> handleAuthException(AuthException ae, HttpServletRequest req) {

        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        logRequestBody(ae.getStatusCode(), ae.getErrorMessage(), reqBody, req.getMethod());

        return responseHandler.createErrorResponse(ae.getErrorVO());
    }

    @ExceptionHandler({ServiceLayerException.class})
    protected ResponseEntity<String> handleServiceLayerException(ServiceLayerException sle, HttpServletRequest req) {

        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        logRequestBody(sle.getStatusCode(), sle.getErrorMessage(), reqBody, req.getMethod());

        return responseHandler.createErrorResponse(sle.getErrorVO());
    }

    @ExceptionHandler({DataLayerException.class})
    protected ResponseEntity<String> handleDataLayerException(DataLayerException dle, HttpServletRequest req) {

        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        logRequestBody(dle.getStatusCode(), dle.getErrorMessage(), reqBody, req.getMethod());

        return responseHandler.createErrorResponse(dle.getErrorVO());
    }

    @ExceptionHandler({RuntimeException.class})
    protected void handleRuntimeException(RuntimeException re) {

        LOGGER.error("Fatal exception occurred: {}", re.getMessage());
        LOGGER.error("Stacktrace: ", re);
        LOGGER.error("Shutting down application");
        System.exit(1);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<String> handleException(Exception e, HttpServletRequest req) {

        try{
            String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
            logRequestBody(500, e.getMessage(), reqBody, req.getMethod());
            ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST,"Error processing request");
            LOGGER.error(errorVO.getErrorMessage(), e);

            return responseHandler.createErrorResponse(errorVO);
        }
        catch (IllegalStateException illegalStateException) {

            String errorMessage = "API path: " + req.getRequestURI() + " unsupported ";
            ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST, errorMessage);
            LOGGER.error(errorVO.getErrorMessage(), illegalStateException);
            return responseHandler.createErrorResponse(errorVO);
        }
    }

    private void logRequestBody(int errorStatusCode, String errorDesc, String reqBody, String httpMethod) {

        if (errorStatusCode == 500 && "POST".equalsIgnoreCase(httpMethod) && !StringUtils.isEmpty(reqBody)) {
            LOGGER.error("for error: " + errorDesc + " - REQUEST-BODY: " + reqBody);
        }
    }
}

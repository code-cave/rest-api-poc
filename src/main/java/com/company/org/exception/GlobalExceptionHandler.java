package com.company.org.exception;

import com.company.org.controller.handler.ResponseHandler;
import com.company.org.error.ErrorVO;
import com.company.org.model.RequestVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
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

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    protected ResponseEntity<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException e,
                                                                         HttpServletRequest req) {
        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        String message = HttpHeaders.ACCEPT + " " + req.getHeader(HttpHeaders.ACCEPT) + " not supported";
        ErrorVO errorVO = new ErrorVO(HttpStatus.NOT_ACCEPTABLE, message);
        logRequestBody(errorVO.getHttpStatus().value(), e.getMessage(), reqBody, req.getMethod());

        return responseHandler.createErrorResponse(errorVO);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    protected ResponseEntity<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e,
                                                                        HttpServletRequest req) {
        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        String message = HttpHeaders.CONTENT_TYPE + " " + req.getHeader(HttpHeaders.CONTENT_TYPE) + " not supported";
        ErrorVO errorVO = new ErrorVO(HttpStatus.UNSUPPORTED_MEDIA_TYPE, message);
        logRequestBody(errorVO.getHttpStatus().value(), e.getMessage(), reqBody, req.getMethod());

        return responseHandler.createErrorResponse(errorVO);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<String> handleException(Exception e, HttpServletRequest req) {

        String reqBody = (String)req.getAttribute(RequestVO.REQUEST_BODY_ATTRIBUTE);
        ErrorVO errorVO = new ErrorVO(HttpStatus.BAD_REQUEST,"Error processing request");
        logRequestBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), reqBody, req.getMethod());
        LOGGER.error(errorVO.getErrorMessage(), e);

        return responseHandler.createErrorResponse(errorVO);
    }

    private void logRequestBody(int errorStatusCode, String errorDesc, String reqBody, String httpMethod) {

        if (errorStatusCode == HttpStatus.INTERNAL_SERVER_ERROR.value() &&
            (HttpMethod.POST.name().equalsIgnoreCase(httpMethod) || HttpMethod.PUT.name().equalsIgnoreCase(httpMethod)) &&
            !StringUtils.isEmpty(reqBody)) {
            LOGGER.error("for error: " + errorDesc + " - REQUEST-BODY: " + reqBody);
        }
    }
}

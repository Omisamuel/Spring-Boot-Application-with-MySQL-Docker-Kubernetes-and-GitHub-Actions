package com.company.inventory.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class IndexController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(ERROR_PATH)
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        WebRequest webRequest = new ServletWebRequest(request);
        Map<String, Object> errorAttributes = getErrorAttributes(webRequest, ErrorAttributeOptions.defaults());
        return ResponseEntity.status(getStatus(request)).body(errorAttributes);
    }

    private Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        DefaultErrorAttributes defaultErrorAttributes = new DefaultErrorAttributes();
        return defaultErrorAttributes.getErrorAttributes(webRequest, options);
    }

    private int getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        return (statusCode != null) ? statusCode : 500;
    }
}

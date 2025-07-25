package com.dbs.common.library.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<LinkedHashMap<String, Object>> handleMaxSizeException(MaxUploadSizeExceededException exc) {
        LinkedHashMap<String, Object> responseMessage = new LinkedHashMap<>();
        responseMessage.put("message", "File too large!");
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(responseMessage);
    }
}

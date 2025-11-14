package com.dbs.module.account.advice;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.ctrl.ResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
class MasterControllerAdvice {
    
    private static final Logger logger = LoggerFactory.getLogger(MasterControllerAdvice.class);

    // fallback method
    @ExceptionHandler(Exception.class) // exception handled
    public ResponseEntity<ResponseObject> handleExceptions(
            Exception e
    ) {
        // ... potential custom logic
        logger.error(Constant.LOG_ERROR, e.getMessage(), e);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500
        e.printStackTrace();
        // converting the stack trace to String
        ResponseObject result = new ResponseObject();

        result.setSuccess(false);
        result.setCode(HttpStatus.INTERNAL_SERVER_ERROR);
        result.setMessage(e.getMessage());
        result.setData(null);
        return new ResponseEntity<>(
                result
                ,
                status
        );
    }

    @ExceptionHandler(MasterException.class)
    public ResponseEntity<MasterErrorResponse> handleCustomDataNotFoundExceptions(
            Exception e
    ) {
        logger.error(Constant.LOG_ERROR, e.getMessage(), e);
        HttpStatus status = HttpStatus.NOT_FOUND; // 404

        return new ResponseEntity<>(
                new MasterErrorResponse(
                        status,
                        e.getMessage(),
                        null // assuming to be in staging environment, otherwise stackTrace should not be valorized
                ),
                status
        );
    }
}
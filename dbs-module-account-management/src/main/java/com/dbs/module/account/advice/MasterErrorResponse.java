package com.dbs.module.account.advice;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import java.util.Date;


public class MasterErrorResponse {
    // customizing timestamp serialization format
    @Getter
    @Setter
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    @Getter
    @Setter
    private int code;

    @Getter
    @Setter
    private String status;

    @Getter
    @Setter
    private String message;

    @Getter
    @Setter
    private String stackTrace;

    @Getter
    @Setter
    private Object data;

    public MasterErrorResponse() {
        timestamp = new Date();
    }

    public MasterErrorResponse(
            HttpStatus httpStatus,
            String message
    ) {
        this();

        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public MasterErrorResponse(
            HttpStatus httpStatus,
            String message,
            String stackTrace
    ) {
        this(
                httpStatus,
                message
        );

        this.stackTrace = stackTrace;
    }

    public MasterErrorResponse(
            HttpStatus httpStatus,
            String message,
            String stackTrace,
            Object data
    ) {
        this(
                httpStatus,
                message,
                stackTrace
        );

        this.data = data;
    }
}
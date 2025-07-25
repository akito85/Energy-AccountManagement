package com.dbs.common.library.ctrl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.*;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@SuppressWarnings("java:S1068")
public class ResponseObject {
    private Boolean success;
    @Setter(AccessLevel.NONE)
    private Integer code;
    private String message;
    private Object data;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private HttpStatus httpCode;

    public ResponseObject() {
    }

    public ResponseObject(Boolean success, Integer code, String message, Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseObject(Boolean success, HttpStatus code, String message, Object data) {
        this.success = success;
        this.code = code.value();
        this.message = message;
        this.data = data;
        this.httpCode = code;
    }

    public void setCode(HttpStatus code) {
        this.code = code.value();
        this.httpCode = code;
    }

    @Override
    public String toString() {
        try {
            Gson gson = new Gson();
            return gson.toJson(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

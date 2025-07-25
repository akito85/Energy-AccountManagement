package com.dbs.module.account.advice;

public class MasterException extends RuntimeException {
    public MasterException() {
        super();
    }

    public MasterException(String message) {
        super(message);
    }
}

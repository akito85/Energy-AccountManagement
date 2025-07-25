package com.dbs.common.library.utils;

public class InvoiceLogEnum {
    public enum InvoiceStatus {
        GENERATING,
        COMPLETED,
        FAILED,
        PENDING,
        INPROGRESS,
        SUCCESS
    }

    public enum InvoiceAction {
        GENERATE,
        REGENERATE
    }
}

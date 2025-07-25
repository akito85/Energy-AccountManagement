package com.dbs.module.account.master.latecharge.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@SuppressWarnings("java:S1068")
public class AttachmentAttribute {
    private MultipartFile fileObject;
    private Integer fileCatgory;
}

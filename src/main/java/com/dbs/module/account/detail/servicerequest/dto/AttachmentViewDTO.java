package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * DTO for viewing Attachment details
 */
@Data
public class AttachmentViewDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String category;
    private String type;
    private String description;
    private String pathFile;
    private String fileName;
    private Long fileSize;
    private Integer fileCategoryId;
    private String fileCategoryName;
    private Boolean isDraft;
    private Boolean isDeleted;
    private String createdBy;
    private Date createdDate;
}

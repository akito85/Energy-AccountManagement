package com.dbs.module.account.detail.servicerequest.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * DTO for attachment submission
 */
@Data
public class AttachmentSubmissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String fileName;
    private Integer fileCategory;
    private Integer fileCategoryId;
    private String description;
    private String category;
    private String type;

    // For file upload
    private String base64Content;  // Base64 encoded file content
    private String filePath;       // If file already uploaded separately
    private Long fileSize;

    // Additional metadata
    private Boolean isDraft = false;
}

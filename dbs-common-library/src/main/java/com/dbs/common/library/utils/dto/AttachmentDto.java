package com.dbs.common.library.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("java:S1068")
public class AttachmentDto implements Serializable {

    private static final long serialVersionUID = 6132163119369895205L;

    private String fileName;
    private String fileType;
    private Integer fileSize;
    private Integer fileCategoryId;
    private String fileCategoryName;
    private String pathFile;
    private String urlFile1;
    private String urlFile2;
    private Integer id;
    private Date createdDate;
    private String createdBy;
    private String updatedBy;
    private Date updatedDate;
    private Boolean isDraft;
}

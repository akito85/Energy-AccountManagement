package com.dbs.module.account.detail.serviceagreement.tossubmission.dto;

import lombok.Data;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.Serializable;
import java.util.Date;

@Data
@SuppressWarnings("java:S1068")
public class AttachmentDto implements Serializable {
    private static final long serialVersionUID = -732749950832378535L;

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

    @Override
    public String toString() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return getClass().getName();
        }
    }
}

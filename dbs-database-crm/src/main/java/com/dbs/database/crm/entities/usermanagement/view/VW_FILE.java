package com.dbs.database.crm.entities.usermanagement.view;

import lombok.Data;
import javax.persistence.*;

@Entity
@Data
@Table(name = "VW_FILE")
public class VW_FILE {
    @Id
    @Column(name="FILE_ID")
    private Integer fileId;

    @Column(name="FILETYPE")
    private String fileType;

    @Column(name="FILENAME")
    private String fileName;

    @Column(name="PATH")
    private String path;
}

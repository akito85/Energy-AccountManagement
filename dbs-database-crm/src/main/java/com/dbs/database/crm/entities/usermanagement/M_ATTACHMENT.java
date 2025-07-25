package com.dbs.database.crm.entities.usermanagement;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_ATTACHMENT")
public class M_ATTACHMENT extends DefaultBaseEntities implements Serializable {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ATTACHMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_ATTACHMENT_SEQ",allocationSize = 1, name = "M_ATTACHMENT_SEQ")
    private Integer id;

    @Column(name = "CATEGORY")
    private String category;

    @Column(name = "TYPE")
    private String type;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "PATH_FILE")
    private String pathFile;

    @Column(name = "REFERENCE_ID")
    private Integer referenceId;

    @Column(name = "FILE_NAME")
    private String fileName;
    
    @Column(name = "FILE_SIZE")
    private long fileSize;
    
    @Column(name = "FILE_CATEGORY_ID")
    private Integer fileCategoryId;
    
    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_DRAFT")
    private Boolean isDraft;
    
    @Convert(converter = BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED")
    private Boolean isDeleted;
    
    
    @ManyToOne
    @JoinColumn(name="PATH_FILE", referencedColumnName = "GLB_VALUE",updatable = false,insertable = false)
    @JsonIgnore
    private R_GLOBAL_TYPE_VALUE pathValue;
    
    @ManyToOne
    @JoinColumn(name="FILE_CATEGORY_ID", referencedColumnName = "GLB_TYPE_VAL_ID",updatable = false,insertable = false)
    @JsonIgnore
    private R_GLOBAL_TYPE_VALUE categoryNameValue;
    
    @Transient
    public String getFileCategoryName() {
    	if(categoryNameValue != null)
    	return getCategoryNameValue().getName();
    	else
    		return null;
    }
    
    @Transient
    public String getPathFile() {
    	if(pathValue !=null)
    
    		return getPathValue().getName();
    	else
    		return null;
    }
}

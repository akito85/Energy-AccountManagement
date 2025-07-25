package com.dbs.database.crm.entities.product;


import lombok.Data;

import javax.persistence.*;

import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "M_PRODUCT_TERM_OF_SERVICE")
public class M_PRODUCT_TERM_OF_SERVICE {

    @Id
    @Column(name = "ID",nullable = false,updatable = false)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_PRODUCT_TERM_OF_SERVICE_SEQ")
    @SequenceGenerator(sequenceName = "M_PRODUCT_TERM_OF_SERVICE_SEQ",allocationSize = 1, name = "M_PRODUCT_TERM_OF_SERVICE_SEQ")
    private Integer id;

    @Column(name = "TOS_ID")
    private Integer tosId;

    @Column(name = "TOS_NAME")
    private String tosName;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "PRODUCT_VERSION_ID")
    private Integer productVersionId;

    @Column(name = "CREATED_DATE")
    private Date createdDate;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;

    @Column(name = "UPDATED_BY")
    private String updatedBy;
    
    @Column(name = "DESCRIPTION")
    private String description;
    
    @Column(name = "IS_DELETED")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isDeleted;
    
    @ManyToOne
	@JoinColumn(name = "PRODUCT_VERSION_ID", referencedColumnName = "ID", updatable = false, insertable = false)
	@JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
	private M_PRODUCT_VERSION mProductVersion;
    
    @OneToMany(mappedBy = "mProductTos")
	@JsonIgnore
	@JsonProperty(value="productTosDetailDtos")
	private List<R_PRODUCT_TOS> productTosDetailDtos;
}

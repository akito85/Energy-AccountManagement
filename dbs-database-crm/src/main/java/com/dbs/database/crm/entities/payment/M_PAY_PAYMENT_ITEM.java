package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "M_PAY_PAYMENT_ITEM")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_PAYMENT_ITEM extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -1695443043745889835L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_PAYMENT_ITEM_SEQ")
    @Column(name = "PAYMENT_ITEM_ID", nullable = false)
    @SequenceGenerator(sequenceName = "M_PAY_PAYMENT_ITEM_SEQ", allocationSize = 1, name = "M_PAY_PAYMENT_ITEM_SEQ")
    private Long id;

    @Column(name = "PAYMENT_ITEM_CODE")
    private String paymentItemCode;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "NAME")
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_BANK_METHOD")
    private Boolean isBankMethod;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;
}

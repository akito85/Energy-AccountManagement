package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_ALLOCATION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_ALLOCATION extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 4734903616681060121L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_ALLOCATION_SEQ")
    @Column(name = "ALLOCATION_ID", nullable = false, length = 10)
    @SequenceGenerator(sequenceName = "R_ALLOCATION_SEQ", allocationSize = 1, name = "R_ALLOCATION_SEQ")
    private Long id;

    @Column(name = "RECEIPT_ID")
    private Long receiptId;

    @Column(name = "ALLOCATION_NUMBER")
    private String allocationNumber;

    @Column(name = "ALLOCATION_TYPE")
    private String allocationType;

    @Column(name = "BILLING_ITEM")
    private String billingItem;

    @Column(name = "BILLING_ITEM_AMOUNT")
    private BigDecimal billingItemAmount;

    @Column(name = "ALLOCATION_AMOUNT")
    private BigDecimal allocationAmount;

    @Column(name = "ALLOCATION_STATUS")
    private String allocationStatus;

    @Column(name = "INVOICE_NUMBER")
    private String invoiceNumber;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ALLOCATION_DATE")
    private Date allocationDate;

    @Column(name = "INVOICE_CURRENCY")
    private String invoiceCurrency;

    @Column(name = "RATE_DATE")
    private Date rateDate;

    @Column(name = "RATE")
    private BigDecimal rate;

    @Column(name = "CONVERTED_CURRENCY")
    private String convertedCurrency;

    @Column(name = "EQUIVALENT_AMOUNT")
    private BigDecimal equivalentAmount;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "IS_DELETED", nullable = false)
    private Boolean isDeleted;

    @Column(name = "BUKU")
    private String buku;

    @Column(name = "INVOICE_PERIOD")
    private String invoicePeriod;

}

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

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "M_PAY_BANK")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_BANK extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 7559703047810018536L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_BANK_SEQ")
    @Column(name = "BANK_ID", nullable = false)
    @SequenceGenerator(sequenceName = "M_PAY_BANK_SEQ", allocationSize = 1, name = "M_PAY_BANK_SEQ")
    private Long id;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "NPWP")
    private String npwp;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "STATUS_APPROVAL")
    private String statusApproval;

    @Column(name = "APPHIER_ID")
    private Integer appHierId;

    @Column(name = "BANK_SHORT_NAME")
    private String bankShortName;

    @Column(name = "IS_BRANCH")
    @Convert(converter= BooleanToYNStringConverter.class)
    private Boolean isBranch;

    @Column(name = "BRANCH_NAME")
    private String branchName;

    @Column(name = "TRIGGER_JSON")
    private String triggerJson;

    @Column(name = "PARTY_ID")
    private Integer partyId;
}

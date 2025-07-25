package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import com.dbs.common.base.utils.Constant;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "R_GL_INFORMATION")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_GL_INFORMATION extends BaseEntities implements Serializable {

    private static final long serialVersionUID = -1618037918974918910L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_GL_INFORMATION_SEQ")
    @Column(name = "GL_INFORMATION_ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_GL_INFORMATION_SEQ", allocationSize = 1, name = "R_GL_INFORMATION_SEQ")
    private Long id;

    @Column(name = "PAYMENT_ITEM_ID")
    private Long paymentItemId;

    @Column(name = "BANK_ACCOUNT_ID")
    private Long bankAccountId;

    @Column(name = "BANK_ACCOUNT")
    private String bankAccount;

    @Column(name = "GL_ACCOUNT")
    private String glAccount;

    @Column(name = "START_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date startDate;

    @Column(name = "END_DATE")
    @JsonFormat(pattern = Constant.FORMAT_START_END_DATE, timezone = Constant.TIMEZONE)
    private Date endDate;
}

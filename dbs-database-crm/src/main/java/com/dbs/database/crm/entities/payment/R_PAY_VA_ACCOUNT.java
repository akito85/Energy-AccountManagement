package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_PAY_VA_ACCOUNT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_VA_ACCOUNT extends DefaultBaseEntities implements Serializable {
    private static final long serialVersionUID = -6143880819311710927L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_VA_ACCOUNT_SEQ")
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_PAY_VA_ACCOUNT_SEQ", allocationSize = 1, name = "R_PAY_VA_ACCOUNT_SEQ")
    private Long id;

    @Column(name = "BANK_ACCOUNT_ID")
    private Long bankAccountId;

    @Column(name = "CUSTOMER_NUMBER")
    private String customerNumber;

    @Column(name = "CUSTOMER_NAME")
    private String customerName;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;

    @Column(name = "VA_NUMBER")
    private String vaNumber;

    @Column(name = "CUSTOMER")
    private String customer;

    @Column(name = "ACCOUNT")
    private String account;
}

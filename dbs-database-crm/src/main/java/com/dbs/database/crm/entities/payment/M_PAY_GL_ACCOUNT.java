package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "M_PAY_GL_ACCOUNT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_GL_ACCOUNT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 2102517961451798409L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_GL_ACCOUNT_SEQ")
    @Column(name = "ID", nullable = false)
    @SequenceGenerator(sequenceName = "M_PAY_GL_ACCOUNT_SEQ", allocationSize = 1, name = "M_PAY_GL_ACCOUNT_SEQ")
    private Long id;

    @Column(name = "ACCOUNT_NUMBER")
    private String accountNumber;

    @Column(name = "ACCOUNT_NAME")
    private String accountName;
}

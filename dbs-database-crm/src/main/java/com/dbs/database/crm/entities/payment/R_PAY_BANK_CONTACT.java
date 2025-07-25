package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.DefaultBaseEntities;
import com.dbs.common.base.utils.BooleanToYNStringConverter;
import com.dbs.database.crm.entities.accountmanagement.M_CONTACT;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "R_PAY_BANK_CONTACT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class R_PAY_BANK_CONTACT extends DefaultBaseEntities implements Serializable {

    private static final long serialVersionUID = 8106997583555170122L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "R_PAY_BANK_CONTACT_SEQ")
    @Column(name = "BANK_CONTACT_ID", nullable = false)
    @SequenceGenerator(sequenceName = "R_PAY_BANK_CONTACT_SEQ", allocationSize = 1, name = "R_PAY_BANK_CONTACT_SEQ")
    private Long id;

    @Column(name = "BANK_ID")
    private Long bankId;

    @Column(name = "BANK_CODE")
    private String bankCode;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "CONTACT_ID", referencedColumnName = "ID", updatable = false)
    private M_CONTACT contact;

    @Convert(converter= BooleanToYNStringConverter.class)
    @Column(name = "PRIMARY_FLAG")
    private Boolean primaryFlag;

    @Column(name = "PARTY_ID")
    private Integer partyId;

    @Column(name = "DESCRIPTION")
    private String description;
}

package com.dbs.database.crm.entities.payment;

import com.dbs.common.base.entities.BaseEntities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "M_PAY_COLLECTING_AGENT")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class M_PAY_COLLECTING_AGENT extends BaseEntities implements Serializable {

    private static final long serialVersionUID = 6595574819201579261L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "M_PAY_COLLECTING_AGENT_SEQ")
    @Column(name = "ID", nullable = false, insertable = false)
    @SequenceGenerator(sequenceName = "M_PAY_COLLECTING_AGENT_SEQ", allocationSize = 1, name = "M_PAY_COLLECTING_AGENT_SEQ")
    private Long id;

    @Column(name = "STATUS_APPROVAL", nullable = false)
    private String statusApproval;

    @Column(name = "NAME", nullable = false)
    private String name;
}

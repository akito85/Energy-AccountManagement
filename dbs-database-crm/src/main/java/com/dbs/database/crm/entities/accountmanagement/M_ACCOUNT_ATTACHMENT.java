package com.dbs.database.crm.entities.accountmanagement;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "M_ACCOUNT_ATTACHMENT")
public class M_ACCOUNT_ATTACHMENT {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "M_ACCOUNT_ATTACHMENT_SEQ")
    @SequenceGenerator(sequenceName = "M_ACCOUNT_ATTACHMENT_SEQ",allocationSize = 1, name = "M_ACCOUNT_ATTACHMENT_SEQ")
    private Integer id;

    @Column(name = "ACCOUNT_ID")
    private Integer accountId;

    @Column(name = "ATTACHMENT_ID")
    private Integer attachmentId;
}

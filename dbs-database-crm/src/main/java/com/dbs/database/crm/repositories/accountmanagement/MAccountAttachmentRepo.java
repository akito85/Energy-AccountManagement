package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_ATTACHMENT;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAccountAttachmentRepo extends JpaRepository<M_ACCOUNT_ATTACHMENT, Integer> {
    List<M_ACCOUNT_ATTACHMENT> findByAccountId(Integer accountId);
}

package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_ATTACHMENT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSAAttachmentRepo extends PagingAndSortingRepository<T_AM_SA_ATTACHMENT, Integer>, JpaSpecificationExecutor<T_AM_SA_ATTACHMENT> {
    List<T_AM_SA_ATTACHMENT> findAllBySaId(Integer saId);
}

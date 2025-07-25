package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.VW_TOS_CATALOG_DTL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTosCatalogDtlRepo extends JpaRepository<VW_TOS_CATALOG_DTL, Integer> {
    List<VW_TOS_CATALOG_DTL> findAll();
    List<VW_TOS_CATALOG_DTL> findAllBySaTosId(Integer saTosId);
}

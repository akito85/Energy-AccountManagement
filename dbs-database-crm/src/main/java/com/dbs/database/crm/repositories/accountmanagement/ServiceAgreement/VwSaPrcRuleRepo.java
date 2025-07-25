package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.VW_SA_PRCRULE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwSaPrcRuleRepo extends JpaRepository<VW_SA_PRCRULE, Integer> , JpaSpecificationExecutor<VW_SA_PRCRULE> {
    List<VW_SA_PRCRULE> findAllBySaId(Integer saId);
}

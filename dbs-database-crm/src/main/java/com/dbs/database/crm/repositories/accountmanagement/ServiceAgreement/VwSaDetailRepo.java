package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.VW_SA_DETAIL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwSaDetailRepo extends JpaRepository<VW_SA_DETAIL, Integer> {
    List<VW_SA_DETAIL> findAllBySaId(Integer saId);
}

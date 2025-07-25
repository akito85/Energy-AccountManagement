package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_LATECHARGE_HEADER;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_PRC_RULE_DTL;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSAPrcRuleDTLRepo extends PagingAndSortingRepository<T_AM_SA_PRC_RULE_DTL, Integer>, JpaSpecificationExecutor<T_AM_SA_PRC_RULE_DTL> {
    List<T_AM_SA_PRC_RULE_DTL> findAllBySaId(Integer saId);

    Optional<T_AM_SA_PRC_RULE_DTL> findByIdMPricingAndSaId(Integer idMPricing, Integer saId);
}

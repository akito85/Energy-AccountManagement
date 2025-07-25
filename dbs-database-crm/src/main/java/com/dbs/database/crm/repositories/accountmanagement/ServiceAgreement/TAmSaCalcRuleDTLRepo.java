package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_CALCRULE_DTL;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_CALCRULE_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSaCalcRuleDTLRepo extends PagingAndSortingRepository<T_AM_SA_CALCRULE_DTL, Integer>, JpaSpecificationExecutor<T_AM_SA_CALCRULE_DTL> {
    List<T_AM_SA_CALCRULE_DTL> findAllBySaCalcruleHeaderId(Integer saCalcHeaderId);
}

package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_PRCADJUSTMENT_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSaPrcAdjustmentHeaderRepo extends PagingAndSortingRepository<T_AM_SA_PRCADJUSTMENT_HEADER,Integer>, JpaSpecificationExecutor<T_AM_SA_PRCADJUSTMENT_HEADER> {
    List<T_AM_SA_PRCADJUSTMENT_HEADER> findAllBySaId(Integer saId);
}

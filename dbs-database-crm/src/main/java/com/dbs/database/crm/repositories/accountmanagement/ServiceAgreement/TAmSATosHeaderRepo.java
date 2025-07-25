package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_CALCRULE_HEADER;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_TOS_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSATosHeaderRepo extends PagingAndSortingRepository<T_AM_SA_TOS_HEADER, Integer>, JpaSpecificationExecutor<T_AM_SA_TOS_HEADER> {
    List<T_AM_SA_TOS_HEADER> findAllBySaId(Integer saId);
}

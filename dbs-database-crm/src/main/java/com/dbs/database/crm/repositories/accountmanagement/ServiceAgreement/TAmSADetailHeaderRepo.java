package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_DETAIL_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSADetailHeaderRepo extends PagingAndSortingRepository<T_AM_SA_DETAIL_HEADER, Integer>, JpaSpecificationExecutor<T_AM_SA_DETAIL_HEADER> {

    Optional<T_AM_SA_DETAIL_HEADER> findBySaId(Integer saId);
}

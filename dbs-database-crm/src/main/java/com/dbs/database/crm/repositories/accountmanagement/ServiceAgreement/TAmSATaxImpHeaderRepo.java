package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_LATECHARGE_HEADER;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_TAXIMP_HEADER;
import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_TOS_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSATaxImpHeaderRepo extends PagingAndSortingRepository<T_AM_SA_TAXIMP_HEADER, Integer>, JpaSpecificationExecutor<T_AM_SA_TAXIMP_HEADER> {
    Optional<T_AM_SA_TAXIMP_HEADER> findBySaId(Integer saId);
    List<T_AM_SA_TAXIMP_HEADER> findAllBySaId(Integer saId);

    List<T_AM_SA_TAXIMP_HEADER> findAllBySaIdAndStatus(Integer saId, String status);

    Optional<T_AM_SA_TAXIMP_HEADER> findTopByTaxImplicationIdAndStatus(Integer taxImplicationId, String status);
}

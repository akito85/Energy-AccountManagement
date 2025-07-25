package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA_LATECHARGE_HEADER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSALateChargeHeaderRepo extends PagingAndSortingRepository<T_AM_SA_LATECHARGE_HEADER, Integer>, JpaSpecificationExecutor<T_AM_SA_LATECHARGE_HEADER> {
    Optional<T_AM_SA_LATECHARGE_HEADER> findBySaId(Integer saId);

    List<T_AM_SA_LATECHARGE_HEADER> findAllBySaId(Integer saId);

    List<T_AM_SA_LATECHARGE_HEADER> findAllBySaIdAndStatus(Integer saId, String status);

    @Query(value = "SELECT LATE_CHARGES_ID FROM T_AM_SA_LATECHARGE_HEADER WHERE T_AM_SA_ID =:saId", nativeQuery = true)
    List<Integer> findLateChargesIdBySaId(Integer saId);

    Optional<T_AM_SA_LATECHARGE_HEADER> findTopBylateChargesIdAndStatus(Integer lateChargesId, String status);
}

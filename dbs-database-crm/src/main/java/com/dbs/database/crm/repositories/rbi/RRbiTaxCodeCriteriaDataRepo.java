package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_TAX_CODE_CRITERIA_DATA;
import java.util.List;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiTaxCodeCriteriaDataRepo extends PagingAndSortingRepository<R_TAX_CODE_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<R_TAX_CODE_CRITERIA_DATA> {

    @Query("SELECT a FROM R_TAX_CODE_CRITERIA_DATA a WHERE a.taxCodeId = :taxCodeId AND a.id NOT IN :id")
    List<R_TAX_CODE_CRITERIA_DATA> findNotIn(Integer taxCodeId, List<Integer> id);
}

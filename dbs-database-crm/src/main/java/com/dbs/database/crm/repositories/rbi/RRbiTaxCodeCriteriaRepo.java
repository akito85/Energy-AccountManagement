package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_TAX_CODE_CRITERIA;
import java.util.List;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiTaxCodeCriteriaRepo extends PagingAndSortingRepository<R_TAX_CODE_CRITERIA, Integer>, JpaSpecificationExecutor<R_TAX_CODE_CRITERIA> {

    
    @Query("SELECT a FROM R_TAX_CODE_CRITERIA a WHERE a.taxCodeId = :taxCodeId AND a.criteria NOT IN :criteria")
    List<R_TAX_CODE_CRITERIA> findNotIn(Integer taxCodeId, List<Integer> criteria);
}

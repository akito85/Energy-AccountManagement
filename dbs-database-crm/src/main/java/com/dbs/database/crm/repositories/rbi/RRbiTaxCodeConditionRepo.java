package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_TAX_CODE_CONDITION;
import java.util.List;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RRbiTaxCodeConditionRepo extends PagingAndSortingRepository<R_RBI_TAX_CODE_CONDITION, Integer>, JpaSpecificationExecutor<R_RBI_TAX_CODE_CONDITION> {

    @Query("SELECT a FROM R_RBI_TAX_CODE_CONDITION a WHERE a.taxCodeId = :taxCodeId AND a.id NOT IN :id")
    List<R_RBI_TAX_CODE_CONDITION> findNotIn(Integer taxCodeId, List<Integer> id);
}

package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_PRODUCT_CALCULATION_RULE;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductCalculationRuleRepo extends PagingAndSortingRepository<M_PRODUCT_CALCULATION_RULE,String>, JpaSpecificationExecutor<M_PRODUCT_CALCULATION_RULE> {

    Page<M_PRODUCT_CALCULATION_RULE> findAllByProductVersionId(Integer id, Pageable var1);

    M_PRODUCT_CALCULATION_RULE findTopById(Integer id);
    
    @Query("SELECT a FROM M_PRODUCT_CALCULATION_RULE a WHERE a.productVersionId = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_CALCULATION_RULE> findNotIn(Integer productId, List<Integer> detailIds);
    
    List<M_PRODUCT_CALCULATION_RULE> findAllByProductVersionId(Integer productVersionId);
    
    
}

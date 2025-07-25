package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_PRODUCT_PRICING;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductPricingRepo extends PagingAndSortingRepository<M_PRODUCT_PRICING,Integer>, JpaSpecificationExecutor<M_PRODUCT_PRICING> {

    Optional<M_PRODUCT_PRICING> findTopByProductVersionId(Integer id);

    Optional<M_PRODUCT_PRICING> findTopById(Integer id);
    
    Optional<M_PRODUCT_PRICING> findTopByPricingRuleId(Integer id);
    
    boolean existsByPricingRuleId(Integer id);
    
    @Query("SELECT a FROM M_PRODUCT_PRICING a WHERE a.productVersionId = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_PRICING> findNotIn(Integer productId, List<Integer> detailIds);
    
    List<M_PRODUCT_PRICING> findAllByProductVersionId(Integer productVersionId);
}

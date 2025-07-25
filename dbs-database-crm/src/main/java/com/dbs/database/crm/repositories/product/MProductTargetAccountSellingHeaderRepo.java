package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_PRODUCT_TARGET_ACCOUNT_SELLING;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductTargetAccountSellingHeaderRepo extends PagingAndSortingRepository<M_PRODUCT_TARGET_ACCOUNT_SELLING,Integer>, JpaSpecificationExecutor<M_PRODUCT_TARGET_ACCOUNT_SELLING> {

    M_PRODUCT_TARGET_ACCOUNT_SELLING findTopByProductVersionId(Integer id);

    M_PRODUCT_TARGET_ACCOUNT_SELLING findTopById(Integer id);
    
    @Query("SELECT a FROM M_PRODUCT_TARGET_ACCOUNT_SELLING a WHERE a.productVersionId = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_TARGET_ACCOUNT_SELLING> findNotIn(Integer productId, List<Integer> detailIds);
}

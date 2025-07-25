package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductTargetAccountSellingCriteriaRepo extends PagingAndSortingRepository<M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA,Integer>, JpaSpecificationExecutor<M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA> {

	M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA findTopByIdTargetAccountSelling(Integer id);

	M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA findTopById(Integer id);
	
	Optional<List<M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA>> findAllByIdTargetAccountSelling(Integer id);
	
	@Query("SELECT a FROM M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA a WHERE a.idTargetAccountSelling = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_TARGET_ACCOUNT_SELLING_CRITERIA> findNotIn(Integer productId, List<Integer> detailIds);
}

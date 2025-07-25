package com.dbs.database.crm.repositories.mastermanagement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.accountmanagement.R_CRITERIA_ADJUSTMENT_PRICING;
import com.dbs.database.crm.entities.accountmanagement.R_PRICING_ADJUSTMENT_DETAIL;


@Repository
@Transactional(value = "crmTransactionManager")
public interface RCriteriaAdjustmentPricingRepo extends PagingAndSortingRepository<R_CRITERIA_ADJUSTMENT_PRICING,Integer>,JpaSpecificationExecutor<R_CRITERIA_ADJUSTMENT_PRICING>{

	List<R_CRITERIA_ADJUSTMENT_PRICING> findAllByIdPricingAdjustment(Integer idPricingAdjustment);
	
	@Query("SELECT a FROM R_CRITERIA_ADJUSTMENT_PRICING a WHERE a.idPricingAdjustment = :pricingAdjustmentId AND a.id NOT IN :detailIds")
    List<R_CRITERIA_ADJUSTMENT_PRICING> findNotIn(Integer pricingAdjustmentId, List<Integer> detailIds);


}

package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE;
import com.dbs.database.crm.utils.CostCenterUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPricingRuleRepo extends PagingAndSortingRepository<M_PRICING_RULE, Integer>, JpaSpecificationExecutor<M_PRICING_RULE> {
    default Specification<M_PRICING_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PRICING_RULE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PRICING_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PRICING_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_PRICING_RULE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PRICING_RULE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PRICING_RULE> addDefaultFilters(Specification<M_PRICING_RULE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_PRICING_RULE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);

        /*INFO: Add Cost Center Filter*/
        specification = (Specification<M_PRICING_RULE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        
        return specification;
    }
    
    List<M_PRICING_RULE> findAll();
    
    List<M_PRICING_RULE> findAllByIsDeleted(String flag);

    M_PRICING_RULE findTopByNameIgnoreCaseAndEntityIdAndCcIdAndIsDeleted(String name, Integer entityId, Integer ccId, String flag);


    M_PRICING_RULE findTopByPricingRuleIdAndEntityIdAndCcIdAndIsDeleted(Integer pricingRuleId, Integer entityId, Integer ccId, String flag);

    M_PRICING_RULE findTopByPricingRuleIdAndEntityIdAndCcIdInAndIsDeleted(Integer pricingRuleId, Integer entityId, List<Integer> ccId, String flag);


    M_PRICING_RULE findTopByPricingRuleIdAndIsDeleted(Integer id, String flag);

    List<M_PRICING_RULE> findAllByEntityIdAndIsDeleted(Integer id, String flag);

    List<M_PRICING_RULE> findAllByEntityIdAndCcIdInAndIsDeleted(Integer entityId, List<Integer> ccId, String flag);

    Optional<M_PRICING_RULE> findByPricingRuleId(Integer pricingRuleId);
    
	
	@Query("SELECT DISTINCT a.pricingRuleId FROM M_PRICING_RULE a WHERE a.status ='ACTIVE' AND (a.endDate > CURRENT_DATE OR a.endDate is null) AND a.startDate <= CURRENT_DATE")
	Optional<List<Integer>> findByEndDate();
    @Query(value = "SELECT mpr.PRICING_RULE_ID FROM M_PRICING_RULE mpr LEFT \n" +
            "JOIN R_PRICING_RULE_CRITERIA rpc ON mpr.PRICING_RULE_ID = rpc.PRICING_RULE_ID \n" +
            "WHERE mpr.STATUS ='ACTIVE' AND mpr.APPROVAL_STATUS ='APPROVED' AND rpc.CRITERIA = :criteriaId", nativeQuery = true)
    List<Integer> findAllPricingRuleByCriteria(Integer criteriaId);

    List<M_PRICING_RULE> findAllByPricingRuleId (Integer id);
}

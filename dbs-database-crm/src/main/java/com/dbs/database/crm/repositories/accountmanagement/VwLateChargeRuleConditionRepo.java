package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_LATE_CHARGE_RULE_CONDITION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwLateChargeRuleConditionRepo extends PagingAndSortingRepository<VW_LATE_CHARGE_RULE_CONDITION, Integer>, JpaSpecificationExecutor<VW_LATE_CHARGE_RULE_CONDITION> {
    default Specification<VW_LATE_CHARGE_RULE_CONDITION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_RULE_CONDITION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_LATE_CHARGE_RULE_CONDITION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_LATE_CHARGE_RULE_CONDITION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_LATE_CHARGE_RULE_CONDITION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_RULE_CONDITION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_LATE_CHARGE_RULE_CONDITION> addDefaultFilters(Specification<VW_LATE_CHARGE_RULE_CONDITION> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
//        if(filter.get("status") != null) {
//            if(specification == null) {
//                specification =(Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR);
//            } else {
//                specification =specification.and((Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
//            }
//        }
        return specification;
    }

    List<VW_LATE_CHARGE_RULE_CONDITION> findAllByLatechargeRuleId(Integer latechargeRuleId);
    List<VW_LATE_CHARGE_RULE_CONDITION> findAllByLatechargeRuleIdOrderByIdAsc(Integer latechargeRuleId);

}

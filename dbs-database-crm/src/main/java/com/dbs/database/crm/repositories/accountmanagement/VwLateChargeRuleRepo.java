package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_LATE_CHARGE_RULE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwLateChargeRuleRepo extends PagingAndSortingRepository<VW_LATE_CHARGE_RULE, Integer>, JpaSpecificationExecutor<VW_LATE_CHARGE_RULE> {
    default Specification<VW_LATE_CHARGE_RULE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_RULE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_LATE_CHARGE_RULE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_LATE_CHARGE_RULE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_RULE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_LATE_CHARGE_RULE> addDefaultFilters(Specification<VW_LATE_CHARGE_RULE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("status") != null) {
            if(specification == null) {
                specification =(Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
            }
        }

        if(filter.get("latechargeId") != null) {
            if(specification == null) {
                specification =(Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("latechargeId~" + filter.get("latechargeId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_LATE_CHARGE_RULE>) PagingUtils.createSpecification("latechargeId~" + filter.get("latechargeId"), EQUALS_SELECTOR));
            }
        }
        return specification;
    }

    Optional<VW_LATE_CHARGE_RULE> findById(Integer id);

    List<VW_LATE_CHARGE_RULE> findAllByLatechargeId(Integer latechargeId);
}

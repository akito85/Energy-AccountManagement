package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_CRITERIA_DATA;
import com.dbs.database.crm.entities.accountmanagement.VW_LATE_CHARGE_CRITERIA_DATA_REAL;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value="crmTransactionManager")
public interface VwLateChargeCriteriaDataRepo extends PagingAndSortingRepository<VW_LATE_CHARGE_CRITERIA_DATA_REAL, Integer>, JpaSpecificationExecutor<VW_LATE_CHARGE_CRITERIA_DATA_REAL> {
    default Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> addDefaultFilters(Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL> specification, Map<String, Object> filter, Boolean isFirst){
//        if(specification == null) {
//    		specification =(Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
//        } else {
//    		specification =specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
//        }
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("lateChargeId"))){
                specification = (Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL>) where(PagingUtils.createSpecification("lateChargeId~"+filter.get("lateChargeId").toString(),EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("lateChargeId"))){
                specification =  specification.and((Specification<VW_LATE_CHARGE_CRITERIA_DATA_REAL>) PagingUtils.createSpecification("lateChargeId~"+filter.get("lateChargeId").toString(),EQUALS_SELECTOR));
            }
        }
        return specification;
    }

    @Query("SELECT a FROM VW_LATE_CHARGE_CRITERIA_DATA_REAL a WHERE a.lateChargeId IN :lateChargeId")
    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> findIn(List<Integer> lateChargeId);
    
    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> findAllByLateChargeIdOrderByIdAsc(Integer lateChargeId);

    List<VW_LATE_CHARGE_CRITERIA_DATA_REAL> findAllByLateChargeId(Integer lateChargeId);
}

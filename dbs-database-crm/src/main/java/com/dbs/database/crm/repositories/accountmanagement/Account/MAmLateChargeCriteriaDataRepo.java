package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE_CRITERIA_DATA;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value="crmTransactionManager")
public interface MAmLateChargeCriteriaDataRepo extends PagingAndSortingRepository<M_AM_LATECHARGE_CRITERIA_DATA, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE_CRITERIA_DATA>{

    default Specification<M_AM_LATECHARGE_CRITERIA_DATA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE_CRITERIA_DATA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_LATECHARGE_CRITERIA_DATA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_LATECHARGE_CRITERIA_DATA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_AM_LATECHARGE_CRITERIA_DATA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE_CRITERIA_DATA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_LATECHARGE_CRITERIA_DATA> addDefaultFilters(Specification<M_AM_LATECHARGE_CRITERIA_DATA> specification, Map<String, Object> filter, Boolean isFirst){
//        if(specification == null) {
//    		specification =(Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
//        } else {
//    		specification =specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
//        }
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("latechargeId"))){
                specification = (Specification<M_AM_LATECHARGE_CRITERIA_DATA>) where(PagingUtils.createSpecification("latechargeId~"+filter.get("latechargeId").toString(),EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("latechargeId"))){
                specification =  specification.and((Specification<M_AM_LATECHARGE_CRITERIA_DATA>) PagingUtils.createSpecification("latechargeId~"+filter.get("latechargeId").toString(),EQUALS_SELECTOR));
            }
        }
        return specification;
    }
    List<M_AM_LATECHARGE_CRITERIA_DATA> findAllByStatus(String status);
    List<M_AM_LATECHARGE_CRITERIA_DATA> findAllByLatechargeId(Integer latechargeId);

    Optional<M_AM_LATECHARGE_CRITERIA_DATA> findById(Integer id);
    
    @Query("SELECT a FROM M_AM_LATECHARGE_CRITERIA_DATA a WHERE a.latechargeId IN :latechargeId")
    List<M_AM_LATECHARGE_CRITERIA_DATA> findIn(List<Integer> latechargeId);

}

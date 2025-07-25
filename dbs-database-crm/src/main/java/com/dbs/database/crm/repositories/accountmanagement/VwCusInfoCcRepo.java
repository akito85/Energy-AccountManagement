package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.AdvanceFilter;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwCusInfoCcRepo extends PagingAndSortingRepository<VW_CUS_INFO_CC,Integer>, JpaSpecificationExecutor<VW_CUS_INFO_CC> {
    default Specification<VW_CUS_INFO_CC> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CUS_INFO_CC> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CUS_INFO_CC>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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
    default Specification<VW_CUS_INFO_CC> getSpecificationFromAdvanceFilters(List<AdvanceFilter> pagingdata, Specification<VW_CUS_INFO_CC> specification) {
//        if(ObjectUtils.isEmpty(specification)){
//            Specification<VW_CUS_INFO_CC> specification = null;
//        }
        //Add Filter From Front End
        for (AdvanceFilter sr : pagingdata) {
            if(sr.getCondition().equalsIgnoreCase("AND")){
                specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
            }else if(sr.getCondition().equalsIgnoreCase("OR")){
                specification = specification.or((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
            }
        }

        return specification;
    }

    default Specification<VW_CUS_INFO_CC> getSpecificationFromAdvanceFiltersWithAccount(List<AdvanceFilter> pagingdata, Specification<VW_CUS_INFO_CC> specification, List<Integer> customerIdFiltering) {
//        if(ObjectUtils.isEmpty(specification)){
//            Specification<VW_CUS_INFO_CC> specification = null;
//        }

        //filtering customer with account
        specification = specification.and((Specification<VW_CUS_INFO_CC>) where(PagingUtils.createINSpecification("customerId",customerIdFiltering)));

        //Add Filter From Front End
        for (AdvanceFilter sr : pagingdata) {
            if(sr.getCondition().equalsIgnoreCase("AND")){
                specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
            }else if(sr.getCondition().equalsIgnoreCase("OR")){
                specification = specification.or((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
            }
        }

        return specification;
    }

    default Specification<VW_CUS_INFO_CC> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CUS_INFO_CC> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CUS_INFO_CC> addDefaultFilters(Specification<VW_CUS_INFO_CC> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_CUS_INFO_CC>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(!ObjectUtils.isEmpty(filter.get("positionId"))){
            if(specification != null) {
                specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("positionId"+"~"+filter.get("positionId").toString(),EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("positionId"+"~"+filter.get("positionId").toString(),EQUALS_SELECTOR);
            }

        }
        if(filter.get("accountCostCenterId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("accountCostCenterId").toString()), GET_CC_CHILD);
            if(specification != null) {
                specification =  specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createINSpecification("accountCostCenterId", ccList));
            } else {
                specification =  (Specification<VW_CUS_INFO_CC>) PagingUtils.createINSpecification("accountCostCenterId", ccList);
            }
        }
        if(!ObjectUtils.isEmpty(filter.get("pagingCustomerCm"))){
            specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("pagingCustomerCm"+"~"+filter.get("pagingCustomerCm").toString(),EQUALS_SELECTOR));
        }
        if(!ObjectUtils.isEmpty(filter.get("pagingCustomerHead"))){
            specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("pagingCustomerHead"+"~"+filter.get("pagingCustomerHead").toString(),EQUALS_SELECTOR));
        }

        return specification;
    }

//    List<VW_CUS_INFO_CC> findAllByEntityIdAndCcIdAndPositionId(Integer entityId, Integer ccId, Integer positionId);
    
    Optional<VW_CUS_INFO_CC> findTopByCustomerId(Integer customerId);

    @Override
    public List<VW_CUS_INFO_CC> findAll();
}

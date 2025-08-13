package com.dbs.database.crm.repositories.summary;

import com.dbs.common.base.utils.AdvanceFilter;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.entities.summary.VW_SUMMARY_ACCOUNT;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwSummaryAccountRepo extends PagingAndSortingRepository<VW_SUMMARY_ACCOUNT,Integer>, JpaSpecificationExecutor<VW_SUMMARY_ACCOUNT> {
    default Specification<VW_SUMMARY_ACCOUNT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_SUMMARY_ACCOUNT> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_SUMMARY_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }
    default Specification<VW_SUMMARY_ACCOUNT> getSpecificationFromAdvanceFilters(List<AdvanceFilter> pagingdata, Map<String, Object> filter) {
        Specification<VW_SUMMARY_ACCOUNT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (AdvanceFilter sr : pagingdata) {
            if(i == 0){
                specification = (Specification<VW_SUMMARY_ACCOUNT>) where(PagingUtils.createSpecification(sr.getColumn()+"~"+ sr.getValue(), sr.getOperator()));
            }else{
                if(sr.getCondition().equalsIgnoreCase("AND")){
                    specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }else if(sr.getCondition().equalsIgnoreCase("OR")){
                    specification = specification.or((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }
            }
            i++;
        }
        specification = specification.and(addDefaultFilters2(specification, filter, true));

        return specification;
    }
    default Specification<VW_SUMMARY_ACCOUNT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_SUMMARY_ACCOUNT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_SUMMARY_ACCOUNT> addDefaultFilters(Specification<VW_SUMMARY_ACCOUNT> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("costCenterId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("costCenterId").toString()), GET_CC_CHILD);
            specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
        }
        if(filter.get("customerId") != null){
            specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
        }
        return specification;
    }
    default Specification<VW_SUMMARY_ACCOUNT> getSpecificationFromFilters2(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_SUMMARY_ACCOUNT> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_SUMMARY_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters2(specification, filter,false);
        return specification;
    }

    default Specification<VW_SUMMARY_ACCOUNT> getSpecificationDefault2(Map<String, Object> filter) {
        Specification<VW_SUMMARY_ACCOUNT> specification = null;
        specification = addDefaultFilters2(specification, filter, true);
        return specification;
    }

    default Specification<VW_SUMMARY_ACCOUNT> addDefaultFilters2(Specification<VW_SUMMARY_ACCOUNT> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
            }
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("costCenterId") != null){
            List<Integer> ccList = (List<Integer>) filter.get("costCenterId");
            if(specification != null) {
                specification =  specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createINSpecification("costCenterId", ccList));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createINSpecification("costCenterId", ccList);
            }
        }
        if(filter.get("customerId") != null){
            if(specification != null) {
                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR);
            }
        }
//        if(filter.get("positionId") != null){
//            CostCenterUtils costCenterUtils = new CostCenterUtils();
//            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
//            if(specification != null) {
//                specification = specification.and((Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
//            } else {
//                specification = (Specification<VW_SUMMARY_ACCOUNT>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
//            }
//        }
        return specification;
    }
}

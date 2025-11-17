package com.dbs.database.crm.repositories.accountmanagement.Account.view;

import com.dbs.common.base.utils.AdvanceFilter;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import com.dbs.database.crm.entities.accountmanagement.view.NX_VW_RELATIONSHIP;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import com.dbs.database.crm.utils.CostCenterUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface ViewRelationshipRepo extends PagingAndSortingRepository<NX_VW_RELATIONSHIP, Integer>, JpaSpecificationExecutor<NX_VW_RELATIONSHIP> {

    @SuppressWarnings("unchecked")
    default Specification<NX_VW_RELATIONSHIP> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<NX_VW_RELATIONSHIP> specification = null;
        int i = 0;
        
        // 1. Tangani Search Filters (sama seperti kode Anda)
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<NX_VW_RELATIONSHIP>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        // 2. Tambahkan Default Filters (memanggil method yang sudah diperbaiki)
        // isFirst dikirim sebagai 'false' karena search filter sudah diproses
        specification = addDefaultFilters(specification, filter, false); 

        return specification;
    }

    default Specification<NX_VW_RELATIONSHIP> getSpecificationDefault(Map<String, Object> filter) {
        Specification<NX_VW_RELATIONSHIP> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<NX_VW_RELATIONSHIP> addDefaultFilters(Specification<NX_VW_RELATIONSHIP> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("costCenterId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("costCenterId").toString()), GET_CC_CHILD);
            specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
        }
        if(filter.get("customerId") != null){
            specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
        }
        return specification;
    }

    Optional<NX_VW_RELATIONSHIP> findById(Integer id);


    // =============================
    default Specification<NX_VW_RELATIONSHIP> getSpecificationFromAdvanceFilters(List<AdvanceFilter> pagingdata, Map<String, Object> filter) {
        Specification<NX_VW_RELATIONSHIP> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (AdvanceFilter sr : pagingdata) {
            if(i == 0){
                specification = (Specification<NX_VW_RELATIONSHIP>) where(PagingUtils.createSpecification(sr.getColumn()+"~"+ sr.getValue(), sr.getOperator()));
            }else{
                if(sr.getCondition().equalsIgnoreCase("AND")){
                    specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }else if(sr.getCondition().equalsIgnoreCase("OR")){
                    specification = specification.or((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification(sr.getColumn(), sr.getValue(), sr.getOperator()));
                }
            }
            i++;
        }
        specification = specification.and(addDefaultFilters2(specification, filter, true));

        return specification;
    }

    default Specification<NX_VW_RELATIONSHIP> addDefaultFilters2(Specification<NX_VW_RELATIONSHIP> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("accountGroup") != null) {
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createAccountGroupFilter(specification, filter.get("accountGroup").toString(), Boolean.TRUE);
            }
        }
        if(filter.get("uniqueAccount") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("uniqueAccount"+"~"+filter.get("uniqueAccount").toString(), EQUALS_SELECTOR);
            }
        }

        if(filter.get("entityId") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
            }

        }
        if(filter.get("customerManagementId") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerManagementId"+"~"+filter.get("customerManagementId").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("costCenterId") != null){
            List<Integer> ccList = (List<Integer>) filter.get("costCenterId");
            if(specification != null) {
                specification =  specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createINSpecification("costCenterId", ccList));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createINSpecification("costCenterId", ccList);
            }
        }
        if(filter.get("customerId") != null){
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createSpecification("customerId"+"~"+filter.get("customerId").toString(), EQUALS_SELECTOR);
            }
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            if(specification != null) {
                specification = specification.and((Specification<NX_VW_RELATIONSHIP>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false));
            } else {
                specification = (Specification<NX_VW_RELATIONSHIP>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, false);
            }
        }
        return specification;
    }

    default Specification<NX_VW_RELATIONSHIP> getSpecificationDefault2(Map<String, Object> filter) {
        Specification<NX_VW_RELATIONSHIP> specification = null;
        specification = addDefaultFilters2(specification, filter, true);
        return specification;
    }

}

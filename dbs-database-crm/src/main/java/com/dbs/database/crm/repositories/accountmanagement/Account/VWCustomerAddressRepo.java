package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_ADDDRESS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWCustomerAddressRepo extends PagingAndSortingRepository<VW_CUSTOMER_ADDDRESS, Integer>, JpaSpecificationExecutor<VW_CUSTOMER_ADDDRESS> {
    default Specification<VW_CUSTOMER_ADDDRESS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CUSTOMER_ADDDRESS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CUSTOMER_ADDDRESS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CUSTOMER_ADDDRESS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CUSTOMER_ADDDRESS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CUSTOMER_ADDDRESS> addDefaultFilters(Specification<VW_CUSTOMER_ADDDRESS> specification, Map<String, Object> filter, Boolean isFirst){

        if(filter.get("status") != null) {
            if(specification == null) {
    		specification =(Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
            }
        }

        if(filter.get("premiseFlag") != null) {
            if(specification == null) {
                specification =(Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("premiseFlag~" + filter.get("premiseFlag"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("premiseFlag~" + filter.get("premiseFlag"), EQUALS_SELECTOR));
            }
        }
        
        if(filter.get("customerId") != null) {
            specification = ((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createCustomerFilter(specification, String.valueOf(Integer.parseInt(filter.get("customerId").toString())), isFirst));
        }
        
        if(filter.get("accountId") != null) {
            if(specification == null) {
    		specification =(Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
            }
        }
        
        if(filter.get("customerManagementId") != null) {
            if(specification == null) {
    		specification =(Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("customerManagementId~" + filter.get("customerManagementId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("customerManagementId~" + filter.get("customerManagementId"), EQUALS_SELECTOR));
            }
        }
        
        if(filter.get("costCenterId") != null) {
            if(specification == null) {
    		specification =(Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("costCenterId~" + filter.get("costCenterId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_CUSTOMER_ADDDRESS>) PagingUtils.createSpecification("costCenterId~" + filter.get("costCenterId"), EQUALS_SELECTOR));
            }
        }

        return specification;
    }
    
    Page<VW_CUSTOMER_ADDDRESS> findAll(Specification<VW_CUSTOMER_ADDDRESS> specification, Pageable paging);
    
    List<VW_CUSTOMER_ADDDRESS> findAll();
    
    List<VW_CUSTOMER_ADDDRESS> findAllByCustomerId(Integer customerId);
    
    Optional<VW_CUSTOMER_ADDDRESS> findTopByAccountId(Integer accountId);

    List<VW_CUSTOMER_ADDDRESS> findAllByAccountIdAndStatusAndPremiseFlagIsTrue(Integer accountId, String status);
}

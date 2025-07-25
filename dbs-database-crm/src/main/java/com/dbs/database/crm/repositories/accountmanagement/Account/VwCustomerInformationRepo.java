package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_CUSTOMER_INFORMATION;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.database.crm.entities.accountmanagement.VW_CUS_INFO_CC;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwCustomerInformationRepo extends PagingAndSortingRepository<VW_CUSTOMER_INFORMATION,Integer>, JpaSpecificationExecutor<VW_CUSTOMER_INFORMATION> {
    default Specification<VW_CUSTOMER_INFORMATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CUSTOMER_INFORMATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CUSTOMER_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CUSTOMER_INFORMATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CUSTOMER_INFORMATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CUSTOMER_INFORMATION> addDefaultFilters(Specification<VW_CUSTOMER_INFORMATION> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("customerType") != null && filter.get("identificationType") != null) {
            if(specification == null) {
//    		    specification = (Specification<VW_CUS_INFO_CC>) where(PagingUtils.createSpecification("customerType~" + filter.get("customerType"), EQUALS_SELECTOR));
                specification = specification.and((Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createSpecification("customerType~"+ filter.get("customerType"), DEFAULT_SELECTOR));
//            	specification = specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("identificationType~" + filter.get("identificationType"), EQUALS_SELECTOR));
                specification = specification.and((Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createSpecification("identificationType~"+ filter.get("identificationType"), DEFAULT_SELECTOR));

            } else {
//    		    specification =specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("customerType~" + filter.get("customerType"), EQUALS_SELECTOR));
                specification = specification.and((Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createSpecification("customerType~"+ filter.get("customerType"), DEFAULT_SELECTOR));
//                specification =specification.and((Specification<VW_CUS_INFO_CC>) PagingUtils.createSpecification("identificationType~" + filter.get("identificationType"), EQUALS_SELECTOR));
                specification = specification.and((Specification<VW_CUSTOMER_INFORMATION>) PagingUtils.createSpecification("identificationType~"+ filter.get("identificationType"), DEFAULT_SELECTOR));
            }
        }
        return specification;
    }
    
    Page<VW_CUSTOMER_INFORMATION> findAll(Specification<VW_CUSTOMER_INFORMATION> specification, Pageable paging);
    
    List<VW_CUSTOMER_INFORMATION> findAll();
    
    Optional<VW_CUSTOMER_INFORMATION> findTopByCustomerTypeAndIdentificationTypeAndCustomerIdentificationNumberAndStatus(Integer customerType, Integer identificationType, String identificationNumber, String status);
    
    Optional<VW_CUSTOMER_INFORMATION> findTopByCustomerId(Integer customerId);
}

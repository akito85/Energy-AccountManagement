package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.NX_M_RELATIONSHIP;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRelationshipRepo extends PagingAndSortingRepository<NX_M_RELATIONSHIP, Integer>, JpaSpecificationExecutor<NX_M_RELATIONSHIP> {
    
    default Specification<NX_M_RELATIONSHIP> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<NX_M_RELATIONSHIP> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<NX_M_RELATIONSHIP>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<NX_M_RELATIONSHIP>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<NX_M_RELATIONSHIP> getSpecificationDefault(Map<String, Object> filter) {
        Specification<NX_M_RELATIONSHIP> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<NX_M_RELATIONSHIP> addDefaultFilters(Specification<NX_M_RELATIONSHIP> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("relationshipCategory") != null) {
            specification = (Specification<NX_M_RELATIONSHIP>) PagingUtils.createSpecification("relationshipCategory~" + (filter.get("relationshipCategory")),DEFAULT_SELECTOR);
        }
        if(filter.get("relationType") != null) {
            specification = (Specification<NX_M_RELATIONSHIP>) PagingUtils.createSpecification("relationType~" + (filter.get("relationType")),DEFAULT_SELECTOR);
        }
        return specification;
    }   
    
    Page<NX_M_RELATIONSHIP> findAll(Specification<NX_M_RELATIONSHIP> specification, Pageable paging);
    
    @Query("SELECT a FROM NX_M_RELATIONSHIP a WHERE a.id NOT IN :id")
    List<NX_M_RELATIONSHIP> findNotIn(List<Integer> id);
    
    List<NX_M_RELATIONSHIP> findAll();
    
    Optional<NX_M_RELATIONSHIP> findById(Integer id);

    // NX_M_RELATIONSHIP findByCustomerId(Integer customerId);
    
    // List<NX_M_RELATIONSHIP> findAllByCustomerType(Integer customerType);

    // List<NX_M_RELATIONSHIP> findAllByCustomerTypeAndIdentificationTypeAndCustomerIdentificationNumber(Integer customerType, Integer identificationType, String identificationNumber);
    
    // Optional<M_CUSTOMER> findByIdentificationTypeAndCustomerIdentificationNumber(Integer identificationType, String identificationNumber);
    
    // Optional<M_CUSTOMER> findByCustomerIdentificationNumber(String customerIdentificationNumber);
    
    // Boolean existsByCustomerIdentificationNumber(String idNumber);
    // Boolean existsByCustomerNumber(String customerNumber);
    
    // List<M_CUSTOMER> findTopByOrderByCustomerIdDesc();
    
    // List<M_CUSTOMER> findTopByCustomerTypeOrderByCustomerIdDesc(Integer customerType);

    // List<M_CUSTOMER> findAllByStatusIgnoreCase(String status);
    
    // @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(CUSTOMER_NUMBER, 4, 10))),0) FROM M_CUSTOMER", nativeQuery = true)
    // Integer findMaxCode();
    
    // @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(CUSTOMER_NUMBER, 4, 10))),0) FROM M_CUSTOMER WHERE CUSTOMER_TYPE =:custType", nativeQuery = true)
    // Integer findMaxCodeByCustomerType(Integer custType);
}
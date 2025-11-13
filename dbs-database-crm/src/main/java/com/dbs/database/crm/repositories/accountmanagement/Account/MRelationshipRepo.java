package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_RELATIONSHIP;
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
public interface MRelationshipRepo extends PagingAndSortingRepository<M_RELATIONSHIP, Integer>, JpaSpecificationExecutor<M_RELATIONSHIP> {
    
    default Specification<M_RELATIONSHIP> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RELATIONSHIP> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RELATIONSHIP>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RELATIONSHIP>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RELATIONSHIP> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RELATIONSHIP> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RELATIONSHIP> addDefaultFilters(Specification<M_RELATIONSHIP> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("relationshipCategory") != null) {
            specification = (Specification<M_RELATIONSHIP>) PagingUtils.createSpecification("relationshipCategory~" + (filter.get("relationshipCategory")),DEFAULT_SELECTOR);
        }
        if(filter.get("relationType") != null) {
            specification = (Specification<M_RELATIONSHIP>) PagingUtils.createSpecification("relationType~" + (filter.get("relationType")),DEFAULT_SELECTOR);
        }
        return specification;
    }   
    
    Page<M_RELATIONSHIP> findAll(Specification<M_RELATIONSHIP> specification, Pageable paging);
    
    @Query("SELECT a FROM M_RELATIONSHIP a WHERE a.id NOT IN :id")
    List<M_RELATIONSHIP> findNotIn(List<Integer> id);
    
    List<M_RELATIONSHIP> findAll();
    
    Optional<M_RELATIONSHIP> findById(Integer id);

    // M_RELATIONSHIP findByCustomerId(Integer customerId);
    
    // List<M_RELATIONSHIP> findAllByCustomerType(Integer customerType);

    // List<M_RELATIONSHIP> findAllByCustomerTypeAndIdentificationTypeAndCustomerIdentificationNumber(Integer customerType, Integer identificationType, String identificationNumber);
    
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
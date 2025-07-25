package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER;
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
public interface MCustomerRepo extends PagingAndSortingRepository<M_CUSTOMER, Integer>, JpaSpecificationExecutor<M_CUSTOMER> {
    
    default Specification<M_CUSTOMER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_CUSTOMER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_CUSTOMER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_CUSTOMER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_CUSTOMER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_CUSTOMER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_CUSTOMER> addDefaultFilters(Specification<M_CUSTOMER> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("customerType") != null) {
            specification = (Specification<M_CUSTOMER>) PagingUtils.createSpecification("customerType~" + (filter.get("customerType")),DEFAULT_SELECTOR);
        }
        if(filter.get("identificationType") != null) {
            specification = (Specification<M_CUSTOMER>) PagingUtils.createSpecification("identificationType~" + (filter.get("identificationType")),DEFAULT_SELECTOR);
        }
        return specification;
    }   
    
    Page<M_CUSTOMER> findAll(Specification<M_CUSTOMER> specification, Pageable paging);
    
    @Query("SELECT a FROM M_CUSTOMER a WHERE a.customerType = :customerType AND a.id NOT IN :id")
    List<M_CUSTOMER> findNotIn(Integer customerType, List<Integer> id);
    
    List<M_CUSTOMER> findAll();
    
    M_CUSTOMER findByCustomerId(Integer customerId);
    
    List<M_CUSTOMER> findAllByCustomerType(Integer customerType);
    
    Optional<M_CUSTOMER> findById(Integer id);
    List<M_CUSTOMER> findAllByCustomerTypeAndIdentificationTypeAndCustomerIdentificationNumber(Integer customerType, Integer identificationType, String identificationNumber);
    Optional<M_CUSTOMER> findByIdentificationTypeAndCustomerIdentificationNumber(Integer identificationType, String identificationNumber);
    Optional<M_CUSTOMER> findByCustomerIdentificationNumber(String customerIdentificationNumber);
    Boolean existsByCustomerIdentificationNumber(String idNumber);
    Boolean existsByCustomerNumber(String customerNumber);
    
    List<M_CUSTOMER> findTopByOrderByCustomerIdDesc();
    
    List<M_CUSTOMER> findTopByCustomerTypeOrderByCustomerIdDesc(Integer customerType);

    List<M_CUSTOMER> findAllByStatusIgnoreCase(String status);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(CUSTOMER_NUMBER, 4, 10))),0) FROM M_CUSTOMER", nativeQuery = true)
    Integer findMaxCode();
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(CUSTOMER_NUMBER, 4, 10))),0) FROM M_CUSTOMER WHERE CUSTOMER_TYPE =:custType", nativeQuery = true)
    Integer findMaxCodeByCustomerType(Integer custType);
}
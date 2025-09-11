package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CONTACT;
import com.dbs.database.crm.entities.accountmanagement.M_CONTACT_DETAILS;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MContactDetailsRepo extends PagingAndSortingRepository<M_CONTACT_DETAILS, Integer>, JpaSpecificationExecutor<M_CONTACT_DETAILS> {
    
    default Specification<M_CONTACT_DETAILS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_CONTACT_DETAILS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_CONTACT_DETAILS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_CONTACT_DETAILS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_CONTACT_DETAILS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_CONTACT_DETAILS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_CONTACT_DETAILS> addDefaultFilters(Specification<M_CONTACT_DETAILS> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("contactId") != null) {
            specification = (Specification<M_CONTACT_DETAILS>) PagingUtils.createSpecification("contactId~" + (filter.get("contactId")),DEFAULT_SELECTOR);
        }
        return specification;
    }
    
    List<M_CONTACT_DETAILS> findAll();
    
    List<M_CONTACT_DETAILS> findAllByContactId(Integer id);

    List<M_CONTACT_DETAILS> findAllByContactIdOrderByContactDetailsIdAsc(Integer id);
    
    Optional<M_CONTACT_DETAILS> findById(Integer id);
    
    M_CONTACT_DETAILS findByContactDetailsId(Integer contactDetailsId);
    
    @Query(value = "SELECT * FROM M_CONTACT_DETAILS a WHERE CONCAT(CONCAT(a.INPUT_TYPE,CONCAT(CONCAT(a.TYPE,a.PREFIX_1), a.PREFIX_2)),a.VALUE) = :fullContact", nativeQuery = true)
    M_CONTACT_DETAILS findByFullContactDetail(String fullContact);
}

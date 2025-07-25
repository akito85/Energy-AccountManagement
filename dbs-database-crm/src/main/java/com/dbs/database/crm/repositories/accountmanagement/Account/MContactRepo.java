package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CONTACT;
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
public interface MContactRepo extends PagingAndSortingRepository<M_CONTACT, Integer>, JpaSpecificationExecutor<M_CONTACT> {
    
    default Specification<M_CONTACT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_CONTACT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_CONTACT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_CONTACT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_CONTACT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_CONTACT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_CONTACT> addDefaultFilters(Specification<M_CONTACT> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }
    
    Page<M_CONTACT> findAll(Specification<M_CONTACT> specification, Pageable paging);
    
    List<M_CONTACT> findAll();
    
    M_CONTACT findTopByCreatedDateNotNullOrderByCreatedDateDesc();
    
    M_CONTACT findByContactId(Integer contactId);
    
    List<M_CONTACT> findAllByContactId(Integer contactId);
    
    @Query("SELECT a FROM M_CONTACT a WHERE a.contactId NOT IN :contactId AND a.status = :status")
    List<M_CONTACT> findNotIn(List<Integer> contactId, String status);
    
    Optional<M_CONTACT> findTopByContactNameAndJobIdAndPositionId(String contactName, Integer jobId, Integer positionId);
    
}

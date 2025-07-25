package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.view.VW_CONTACT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwContactRepo extends PagingAndSortingRepository<VW_CONTACT, Integer>, JpaSpecificationExecutor<VW_CONTACT> {
    default Specification<VW_CONTACT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CONTACT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CONTACT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CONTACT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CONTACT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CONTACT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CONTACT> addDefaultFilters(Specification<VW_CONTACT> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/

//        if(filter.get("status") != null) {
//            if(specification == null) {
//                specification =(Specification<VW_CONTACT>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR);
//            } else {
//                specification =specification.and((Specification<VW_CONTACT>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
//            }
//        }
        return specification;
    }

    Optional<VW_CONTACT> findById(Integer id);

//
//    @Query(value = "CALL PGNBILL.PR_CHOOSE_CONTACT(:accountId)", nativeQuery = true)
//    List<VW_CHOOSE_CONTACT> findContactsByAccountId(Integer accountId);
}

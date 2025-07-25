package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_ANNOUNCEMENT;
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
public interface MAnnouncementRepo extends PagingAndSortingRepository<M_ANNOUNCEMENT,Integer>, JpaSpecificationExecutor<M_ANNOUNCEMENT> {

    default Specification<M_ANNOUNCEMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ANNOUNCEMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ANNOUNCEMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ANNOUNCEMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_ANNOUNCEMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ANNOUNCEMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ANNOUNCEMENT> addDefaultFilters(Specification<M_ANNOUNCEMENT> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/

//        if(specification == null) {
//            specification =(Specification<M_ANNOUNCEMENT>) PagingUtils.createSpecification("entityId~" + filter.get("entityId"), EQUALS_SELECTOR);
//        } else {
//            specification =specification.and((Specification<M_ANNOUNCEMENT>) PagingUtils.createSpecification("entityId~" + filter.get("entityId"), EQUALS_SELECTOR));
//        }
        return specification;
    }

    Optional<M_ANNOUNCEMENT> findByAnnNameIgnoreCase(String annName);

    Optional<M_ANNOUNCEMENT> findById(Integer id);

    List<M_ANNOUNCEMENT> findAll();
    
    boolean existsByAnnNameIgnoreCase(String annName);

}

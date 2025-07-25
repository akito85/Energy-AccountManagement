package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_LOCATION;
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
public interface VWLocationRepo extends PagingAndSortingRepository<VW_LOCATION, String>, JpaSpecificationExecutor<VW_LOCATION> {
//    default Specification<VW_LOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
//        Specification<VW_LOCATION> specification = null;
//        //Add Filter From Front End
//        int i = 0;
//        for (String sr : pagingdata.getSearch()) {
//            specification =
//                    i == 0 ?
//                            (Specification<VW_LOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
//                            : specification.and((Specification<VW_LOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
//            i++;
//        }
//        /*------------------------------------------*/
//        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
//
//        return specification;
//    }
//
//    default Specification<VW_LOCATION> getSpecificationDefault(Map<String, Object> filter) {
//        return null;
//    }
//
//    default Specification<VW_LOCATION> addDefaultFilters(Specification<VW_LOCATION> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/
//        if(filter.get("locationTypeId") != null) {
//            if(specification == null) {
//                specification =(Specification<VW_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR);
//            } else {
//                specification =specification.and((Specification<VW_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR));
//            }
//        }
//        return specification;
//    }
default Specification<VW_LOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
    Specification<VW_LOCATION> specification = null;
    //Add Filter From Front End
    int i = 0;
    for (String sr : pagingdata.getSearch()) {
        specification =
                i == 0 ?
                        (Specification<VW_LOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                        : specification.and((Specification<VW_LOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_LOCATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_LOCATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_LOCATION> addDefaultFilters(Specification<VW_LOCATION> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("locationTypeId") != null) {
            if(specification == null) {
                specification =(Specification<VW_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<VW_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR));
            }
        }

        return specification;
    }
    List<VW_LOCATION> findAllBy();

    Optional<VW_LOCATION> findByLocationId(Integer id);
}

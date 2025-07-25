package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION_RULE;
import com.dbs.database.crm.entities.accountmanagement.M_ASSETS;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ASSET;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwChooseAssetRepo extends PagingAndSortingRepository<VW_CHOOSE_ASSET, Integer>, JpaSpecificationExecutor<VW_CHOOSE_ASSET> {
    default Specification<VW_CHOOSE_ASSET> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_CHOOSE_ASSET> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_CHOOSE_ASSET>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_CHOOSE_ASSET>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_CHOOSE_ASSET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_CHOOSE_ASSET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_CHOOSE_ASSET> addDefaultFilters(Specification<VW_CHOOSE_ASSET> specification, Map<String, Object> filter, Boolean isFirst){
//        if(filter.get("isChoose")!=null) {
//            if(specification == null) {
//                specification =(Specification<VW_CHOOSE_ASSET>) PagingUtils.createSpecification("isChoose~" + filter.get("isChoose"), EQUALS_SELECTOR);
//            } else {
//                specification =specification.and((Specification<VW_CHOOSE_ASSET>) PagingUtils.createSpecification("isChoose~" + filter.get("isChoose"), EQUALS_SELECTOR));
//            }
//        }

        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("ready"))){
                specification = (Specification<VW_CHOOSE_ASSET>) where(PagingUtils.createSpecification("ready~"+filter.get("ready"),DEFAULT_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("ready"))){
                specification =  specification.and((Specification<VW_CHOOSE_ASSET>) PagingUtils.createSpecification("ready~"+filter.get("ready"),DEFAULT_SELECTOR));
            }
        }

        return specification;
    }

    @Query("SELECT a FROM VW_CHOOSE_ASSET a WHERE a.status = :status1 OR a.status = :status2 ORDER BY a.createdDate DESC")
    List<VW_CHOOSE_ASSET> findAllByStatus(String status1, String status2);
}

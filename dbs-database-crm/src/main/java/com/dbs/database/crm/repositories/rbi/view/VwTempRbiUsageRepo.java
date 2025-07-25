package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_TEMP_RBI_USAGE;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_USAGE_LIST;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface VwTempRbiUsageRepo extends PagingAndSortingRepository<VW_TEMP_RBI_USAGE,Integer>, JpaSpecificationExecutor<VW_TEMP_RBI_USAGE> {
    default Specification<VW_TEMP_RBI_USAGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TEMP_RBI_USAGE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TEMP_RBI_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TEMP_RBI_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_TEMP_RBI_USAGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TEMP_RBI_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TEMP_RBI_USAGE> addDefaultFilters(Specification<VW_TEMP_RBI_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("isDeleted"))){
                specification = (Specification<VW_TEMP_RBI_USAGE>) where(PagingUtils.createSpecification("isDeleted~"+filter.get("isDeleted").toString(), EQUALS_SELECTOR));
            }
            if(!ObjectUtils.isEmpty(filter.get("batchId"))){
                specification = specification.and((Specification<VW_TEMP_RBI_USAGE>) PagingUtils.createSpecification("batchId~"+filter.get("batchId").toString(), EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("isDeleted"))){
                specification = specification.and((Specification<VW_TEMP_RBI_USAGE>)PagingUtils.createSpecification("isDeleted~"+filter.get("isDeleted").toString(), EQUALS_SELECTOR));
            }
            if(!ObjectUtils.isEmpty(filter.get("batchId"))){
                specification = specification.and((Specification<VW_TEMP_RBI_USAGE>) PagingUtils.createSpecification("batchId~"+filter.get("batchId").toString(), EQUALS_SELECTOR));
            }
        }

        return specification;
    }

    List<VW_TEMP_RBI_USAGE> findAllByBatchIdAndStatus(Integer batchId, String status);
}

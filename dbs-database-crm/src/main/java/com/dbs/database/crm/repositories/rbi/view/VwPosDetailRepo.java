package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_POS_DETAIL;
import com.dbs.database.crm.repositories.rbi.RRbiPosDetailRepo;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface VwPosDetailRepo extends PagingAndSortingRepository<VW_POS_DETAIL, Integer>, JpaSpecificationExecutor<VW_POS_DETAIL> {
    default Specification<VW_POS_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_POS_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<VW_POS_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_POS_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_POS_DETAIL> getSpecificationDefault(Map<String, Object> filter){
        Specification<VW_POS_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_POS_DETAIL> addDefaultFilters(Specification<VW_POS_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(specification != null ){
            if(filter.get("posNumber") != null) {
                specification =specification.and((Specification<VW_POS_DETAIL>) PagingUtils.createSpecification("posNumber~" + filter.get("posNumber"), EQUALS_SELECTOR));
            }
        }else{
            specification = ((Specification<VW_POS_DETAIL>) PagingUtils.createSpecification("posNumber~" + filter.get("posNumber"), EQUALS_SELECTOR));
        }
        return specification;
    }
    List<VW_POS_DETAIL> findAll();
    Optional<VW_POS_DETAIL> findByPosDetailId(Integer posDetailId);
    List<VW_POS_DETAIL> findAllByPosNumber(String posNumber);
}

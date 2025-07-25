package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_USAGE;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_USAGE_LIST;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static org.springframework.data.jpa.domain.Specification.where;

public interface VwUsageList extends PagingAndSortingRepository<VW_USAGE_LIST, Integer>, JpaSpecificationExecutor<VW_USAGE_LIST> {
    default Specification<VW_USAGE_LIST> getSpecficationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_USAGE_LIST> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            String[] searchs = sr.trim().split("~");
            specification =
                    i == 0 ? (Specification<VW_USAGE_LIST>) where(PagingUtils.createSpecification(sr,DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_USAGE_LIST>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_USAGE_LIST> getSpecificationDefault(Map<String, Object> filter){
        Specification<VW_USAGE_LIST> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_USAGE_LIST> addDefaultFilters(Specification<VW_USAGE_LIST> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("entityId") != null){
            specification = (Specification<VW_USAGE_LIST>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = (Specification<VW_USAGE_LIST>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, isFirst);
        }
        if(filter.get("ratingCode") !=null){
            specification = specification.and((Specification<VW_USAGE_LIST>) PagingUtils.createSpecification("ratingCode~"+filter.get("ratingCode"), DEFAULT_SELECTOR));
        }
        return specification;
    }
    List<VW_USAGE_LIST> findAll ();
}

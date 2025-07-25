package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_DAILY_RATE;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_GENERAL_TEMPLATE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwGeneralTemplateRepo extends PagingAndSortingRepository<VW_GENERAL_TEMPLATE, Integer>, JpaSpecificationExecutor<VW_GENERAL_TEMPLATE> {
    default Specification<VW_GENERAL_TEMPLATE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_GENERAL_TEMPLATE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_GENERAL_TEMPLATE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_GENERAL_TEMPLATE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_GENERAL_TEMPLATE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_GENERAL_TEMPLATE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_GENERAL_TEMPLATE> addDefaultFilters(Specification<VW_GENERAL_TEMPLATE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_GENERAL_TEMPLATE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_GENERAL_TEMPLATE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }
        return specification;
    }

    List<VW_GENERAL_TEMPLATE> findAll();
}

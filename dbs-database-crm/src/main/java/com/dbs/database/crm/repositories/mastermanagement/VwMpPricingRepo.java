package com.dbs.database.crm.repositories.mastermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.mastermanagement.VW_MP_PRICING;
import com.dbs.database.crm.utils.CostCenterUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwMpPricingRepo extends PagingAndSortingRepository<VW_MP_PRICING,Integer>, JpaSpecificationExecutor<VW_MP_PRICING> {
    default Specification<VW_MP_PRICING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_MP_PRICING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_MP_PRICING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_MP_PRICING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_MP_PRICING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_MP_PRICING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_MP_PRICING> addDefaultFilters(Specification<VW_MP_PRICING> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_MP_PRICING>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);

        /*INFO: Add Cost Center Filter*/
        specification = (Specification<VW_MP_PRICING>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    List<VW_MP_PRICING> findAllByEntityIdAndCcIdIn(Integer entityId, List<Integer> ccId);
}

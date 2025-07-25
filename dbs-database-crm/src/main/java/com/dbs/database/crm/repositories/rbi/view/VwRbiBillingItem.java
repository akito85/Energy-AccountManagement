package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_BILLING_ITEM;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import com.dbs.database.crm.utils.CostCenterUtils;
import java.util.List;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwRbiBillingItem extends PagingAndSortingRepository<VW_RBI_BILLING_ITEM, Integer>, JpaSpecificationExecutor<VW_RBI_BILLING_ITEM> {
    default Specification<VW_RBI_BILLING_ITEM> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_RBI_BILLING_ITEM> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()){
            if (i == 0){
                specification = (Specification<VW_RBI_BILLING_ITEM>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            } else {
                specification = specification.and((Specification<VW_RBI_BILLING_ITEM>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            }
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_RBI_BILLING_ITEM> getSpecificationDefault(Map<String, Object> filter){
        Specification<VW_RBI_BILLING_ITEM> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_BILLING_ITEM> addDefaultFilters(Specification<VW_RBI_BILLING_ITEM> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_RBI_BILLING_ITEM>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_RBI_BILLING_ITEM>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        /*INFO: Add Cost Center Filter*/
        return specification;
    }
}

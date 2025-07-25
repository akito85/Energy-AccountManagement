package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING_BUCKET;
import com.dbs.database.crm.entities.ratingbillinginvoice.VW_BILLING_BUCKET;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_BILLING_ITEM;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface VwBillingBucketRepo extends PagingAndSortingRepository<VW_BILLING_BUCKET, Integer>, JpaSpecificationExecutor<VW_BILLING_BUCKET> {
    default Specification<VW_BILLING_BUCKET> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<VW_BILLING_BUCKET> specification = null;

        int i=0;
        for(String sr : pagingData.getSearch()){
            specification =
                    i == 0 ?
                            (Specification<VW_BILLING_BUCKET>) where(PagingUtils.createSpecification(sr,DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_BILLING_BUCKET>) PagingUtils.createSpecification(sr,DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification,filter,false);
        return specification;
    }

    default Specification<VW_BILLING_BUCKET> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_BILLING_BUCKET> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }
    default Specification<VW_BILLING_BUCKET> addDefaultFilters(Specification<VW_BILLING_BUCKET> specification, Map<String, Object> filter, Boolean isFirst){
        if(!ObjectUtils.isEmpty(filter.get("entityId"))){
            specification = isFirst ?
                    (Specification<VW_BILLING_BUCKET>) where(PagingUtils.createSpecification("entityId~"+filter.get("entityId").toString(),EQUALS_SELECTOR))
                    : specification.and((Specification<VW_BILLING_BUCKET>) PagingUtils.createSpecification("entityId~"+filter.get("entityId").toString(),EQUALS_SELECTOR));
        }

        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_BILLING_BUCKET>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        if(!ObjectUtils.isEmpty(filter.get("costCenter"))){
            List<Integer> listCostCenter = (List<Integer>) filter.get("costCenter");
            specification = (Specification<VW_BILLING_BUCKET>) PagingUtils.createCostCenterFilter(specification, listCostCenter, isFirst);
        }
        if(specification == null) {
            if (!ObjectUtils.isEmpty(filter.get("listCode"))) {
                specification = (Specification<VW_BILLING_BUCKET>) where(PagingUtils.createINSpecification("billingBucketCode", (List<Integer>) filter.get("listCode")));
            }
        } else {
            if(!ObjectUtils.isEmpty(filter.get("listCode"))){
                specification = specification.and((Specification<VW_BILLING_BUCKET>) where(PagingUtils.createINSpecification("billingBucketCode", (List<Integer>)filter.get("listCode"))));
            }
        }
        return specification;
    }

    List<VW_BILLING_BUCKET> findAllByEntityIdAndCcIdIn(Integer entityId, List<Integer> ccList);
}

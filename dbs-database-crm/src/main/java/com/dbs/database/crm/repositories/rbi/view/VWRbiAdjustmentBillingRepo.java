package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_ADJUSTMENT_BILLING;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static org.springframework.data.jpa.domain.Specification.where;

//@Repository
//@Transactional(value = "crmTransactionManager")
public interface VWRbiAdjustmentBillingRepo extends PagingAndSortingRepository<VW_RBI_ADJUSTMENT_BILLING, Integer>, JpaSpecificationExecutor<VW_RBI_ADJUSTMENT_BILLING> {
    
    default Specification<VW_RBI_ADJUSTMENT_BILLING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_ADJUSTMENT_BILLING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_ADJUSTMENT_BILLING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_ADJUSTMENT_BILLING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RBI_ADJUSTMENT_BILLING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_ADJUSTMENT_BILLING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_ADJUSTMENT_BILLING> addDefaultFilters(Specification<VW_RBI_ADJUSTMENT_BILLING> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_RBI_ADJUSTMENT_BILLING>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        specification = (Specification<VW_RBI_ADJUSTMENT_BILLING>) PagingUtils.createIsDeletedFilter(specification, Boolean.FALSE, isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_CHILD);
            specification = (Specification<VW_RBI_ADJUSTMENT_BILLING>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        return specification;
    }
    
    @Override
    List<VW_RBI_ADJUSTMENT_BILLING> findAll();
    
    List<VW_RBI_ADJUSTMENT_BILLING> findAllByIsDeleted(Boolean isDeleted);
    
    List<VW_RBI_ADJUSTMENT_BILLING> findAllByAccountNumber(String acc);
    
    List<VW_RBI_ADJUSTMENT_BILLING> findAllByCustomerNumber(String acc);
    
}

package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_INVOICE_LIST;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwRbiInvoiceListRepo extends PagingAndSortingRepository<VW_RBI_INVOICE_LIST,String>, JpaSpecificationExecutor<VW_RBI_INVOICE_LIST> {

    default Specification<VW_RBI_INVOICE_LIST> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_LIST> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_INVOICE_LIST>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_INVOICE_LIST>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RBI_INVOICE_LIST> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_LIST> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_INVOICE_LIST> addDefaultFilters(Specification<VW_RBI_INVOICE_LIST> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_RBI_INVOICE_LIST>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_CHILD);
            specification = (Specification<VW_RBI_INVOICE_LIST>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }
        return specification;
    }
    Optional<VW_RBI_INVOICE_LIST> findByInvoiceNumberAndEntityId(String invNumber, Integer entityId);
    List<VW_RBI_INVOICE_LIST> findAllByBillingPeriodName(String billingPeriodName);
    List<VW_RBI_INVOICE_LIST> findByAccountNumberAndBillingCycleAndBillingPeriodNameAndCcIdIn(String accountNumber,String billingCycle, String billingPeriodName,List<Integer> ccList);

}

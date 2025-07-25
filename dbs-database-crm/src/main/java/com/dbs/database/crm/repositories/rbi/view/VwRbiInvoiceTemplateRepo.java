package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_INVOICE_TEMPLATE;
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
public interface VwRbiInvoiceTemplateRepo extends PagingAndSortingRepository<VW_RBI_INVOICE_TEMPLATE, Integer>, JpaSpecificationExecutor<VW_RBI_INVOICE_TEMPLATE> {

    @SuppressWarnings("unchecked")
    default Specification<VW_RBI_INVOICE_TEMPLATE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_TEMPLATE> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_INVOICE_TEMPLATE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_INVOICE_TEMPLATE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_RBI_INVOICE_TEMPLATE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_TEMPLATE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_RBI_INVOICE_TEMPLATE> addDefaultFilters(Specification<VW_RBI_INVOICE_TEMPLATE> specification,
                                                                 Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_RBI_INVOICE_TEMPLATE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if (filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_PARENT);
            specification = (Specification<VW_RBI_INVOICE_TEMPLATE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }
        return specification;
    }
}

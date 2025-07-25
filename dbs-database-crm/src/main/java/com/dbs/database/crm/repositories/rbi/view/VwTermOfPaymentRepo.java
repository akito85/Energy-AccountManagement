package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_RATE_TYPE;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_TERM_OF_PAYMENT;
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
public interface VwTermOfPaymentRepo extends PagingAndSortingRepository<VW_TERM_OF_PAYMENT, Integer>, JpaSpecificationExecutor<VW_TERM_OF_PAYMENT> {
    default Specification<VW_TERM_OF_PAYMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TERM_OF_PAYMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TERM_OF_PAYMENT>) where(PagingUtils.createSpecification(sr,DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TERM_OF_PAYMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_TERM_OF_PAYMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TERM_OF_PAYMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TERM_OF_PAYMENT> addDefaultFilters(Specification<VW_TERM_OF_PAYMENT> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_TERM_OF_PAYMENT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_TERM_OF_PAYMENT>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        return specification;
    }
}
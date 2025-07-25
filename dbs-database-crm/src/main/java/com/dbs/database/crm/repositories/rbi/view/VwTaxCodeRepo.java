package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import java.util.List;

import static com.dbs.common.base.utils.Constant.*;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_TAX_CODE;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTaxCodeRepo extends PagingAndSortingRepository<VW_TAX_CODE,Integer>, JpaSpecificationExecutor<VW_TAX_CODE> {
    default Specification<VW_TAX_CODE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_TAX_CODE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_TAX_CODE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_TAX_CODE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }

    default Specification<VW_TAX_CODE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_TAX_CODE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_TAX_CODE> addDefaultFilters(Specification<VW_TAX_CODE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_TAX_CODE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_TAX_CODE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        /*INFO: Add Cost Center Filter*/
        return specification;
    }

    @Override
    public List<VW_TAX_CODE> findAll();
    
}


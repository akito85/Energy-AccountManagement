package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_POS_LIST;
import java.util.List;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPosListRepo extends PagingAndSortingRepository<VW_POS_LIST,Integer>, JpaSpecificationExecutor<VW_POS_LIST> {
    default Specification<VW_POS_LIST> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_POS_LIST> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_POS_LIST>) where(PagingUtils.createSpecificationRbi(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_POS_LIST>) PagingUtils.createSpecificationRbi(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }

    default Specification<VW_POS_LIST> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_POS_LIST> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_POS_LIST> addDefaultFilters(Specification<VW_POS_LIST> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_POS_LIST>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_CHILD);
            specification = (Specification<VW_POS_LIST>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        /*INFO: Add Cost Center Filter*/
        return specification;
    }
    
    Optional<List<VW_POS_LIST>> findAllByStatusApproval(String statusApproval);
    @Query("SELECT a FROM VW_POS_LIST a WHERE a.statusApproval ='WAITING APPROVAL' AND a.ccId IN :ccList")
    List<VW_POS_LIST> findAvailableApprove(List<Integer> ccList);
    @Override
    List<VW_POS_LIST> findAll();
}


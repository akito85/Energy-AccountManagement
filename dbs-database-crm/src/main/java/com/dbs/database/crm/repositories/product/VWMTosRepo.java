package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.VW_M_TOS;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value = "crmTransactionManager")
public interface VWMTosRepo extends PagingAndSortingRepository<VW_M_TOS,String>, JpaSpecificationExecutor<VW_M_TOS> {

    default Specification<VW_M_TOS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_M_TOS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_M_TOS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_M_TOS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_M_TOS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_M_TOS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_M_TOS> addDefaultFilters(Specification<VW_M_TOS> specification, Map<String, Object> filter, Boolean isFirst){

        specification = (Specification<VW_M_TOS>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
        specification = (Specification<VW_M_TOS>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        
        if (filter.get("status") != null) {
            specification = specification.and((Specification<VW_M_TOS>) PagingUtils.createSpecification("status~" + filter.get("status"), EQUALS_SELECTOR));
        }
        return specification;
    }

    Optional<VW_M_TOS> findTopById(Integer id);
    
    Optional <VW_M_TOS> findById(Integer id);
    
    List<VW_M_TOS> findAllByStatus(String status);
    
    
    List<VW_M_TOS> findAll();
}

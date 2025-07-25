package com.dbs.database.crm.repositories.rbi;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_RATE_TYPE;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_DAILY_RATE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiRateTypeRepo extends PagingAndSortingRepository<M_RBI_RATE_TYPE, Integer>, JpaSpecificationExecutor<M_RBI_RATE_TYPE> {
    default Specification<M_RBI_RATE_TYPE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_RATE_TYPE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_RATE_TYPE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_RATE_TYPE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_RATE_TYPE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_RATE_TYPE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_RATE_TYPE> addDefaultFilters(Specification<M_RBI_RATE_TYPE> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<M_RBI_RATE_TYPE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<M_RBI_RATE_TYPE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        return specification;
    }
    Optional<M_RBI_RATE_TYPE> findByTypeIdAndStatus(Integer typeId, String status);
    Optional<M_RBI_RATE_TYPE> findByCodeAndStatus(String code, String status);
    @Override
    List<M_RBI_RATE_TYPE> findAll();
    List<M_RBI_RATE_TYPE> findAllByStatus(String status);
    @Query(value = "SELECT a FROM M_RBI_RATE_TYPE a WHERE LOWER(a.code) = :code")
    List<M_RBI_RATE_TYPE> findAllByCode(String code);

    Optional<M_RBI_RATE_TYPE> findByCodeIgnoreCase (String code);
}

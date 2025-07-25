package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_USAGE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

public interface MRBIUsageRepo extends PagingAndSortingRepository<M_RBI_USAGE, Integer>, JpaSpecificationExecutor<M_RBI_USAGE> {
    default Specification<M_RBI_USAGE> getSpecficationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter){
        Specification<M_RBI_USAGE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<M_RBI_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_RBI_USAGE> getSpecificationDefault(Map<String, Object> filter){
        Specification<M_RBI_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_USAGE> addDefaultFilters(Specification<M_RBI_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(!ObjectUtils.isEmpty(filter.get("entityId"))){
            specification = (Specification<M_RBI_USAGE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        if(!ObjectUtils.isEmpty(filter.get("positionId"))){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = (Specification<M_RBI_USAGE>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, isFirst);
        }
        if(filter.get("ratingCode") !=null){
            specification = specification.and((Specification<M_RBI_USAGE>) PagingUtils.createSpecification("ratingCode~"+filter.get("ratingCode"), DEFAULT_SELECTOR));
        }
        /*INFO: Add Usage Filter*/
        return specification;
    }

    @Query(value = "SELECT 1 FROM M_RBI_USAGE mru WHERE mru.ACCOUNT_NUMBER = :accountNumber AND mru.ASSET_SERIAL_NUM = :assetSerialNumber AND mru.MEAS_DATE = TO_TIMESTAMP(:measDate, 'DD Mon YYYY HH24:MI:SS') AND mru.TAXATION_ROW_ID = :taxationRowId AND mru.SOURCES = :sources", nativeQuery = true)
    List<Object[]> usageValidation(String accountNumber, String assetSerialNumber, String measDate, String taxationRowId, String sources);

    List<M_RBI_USAGE> findAll();
    List<M_RBI_USAGE> findAllByRatingCode(String ratingCode);
    @Query(nativeQuery = true, value = "SELECT COUNT (*) FROM M_RBI_USAGE RU WHERE RU.ACCOUNT_NUMBER = :accNumb AND RU.BILLING_PERIOD = :billPeriod")
    Long findByAccountNumberAndBillingPeriod (String accNumb, Integer billPeriod);
}

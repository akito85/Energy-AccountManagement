package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_METER_READING_CODES;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;

import static com.dbs.common.base.utils.Constant.*;
import static org.springframework.data.jpa.domain.Specification.where;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Repository
@Transactional(value="crmTransactionManager")
public interface MMeterReadingCodesRepo extends PagingAndSortingRepository<M_METER_READING_CODES, Integer>, JpaSpecificationExecutor<M_METER_READING_CODES>, JpaRepository<M_METER_READING_CODES, Integer> {
    
    default Specification<M_METER_READING_CODES> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_METER_READING_CODES> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_METER_READING_CODES>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_METER_READING_CODES>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_METER_READING_CODES> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_METER_READING_CODES> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_METER_READING_CODES> addDefaultFilters(Specification<M_METER_READING_CODES> specification, Map<String, Object> filter, Boolean isFirst){
        if(filter.get("positionId") != null){
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()), GET_CC_CHILD);
            specification = specification == null ? (Specification<M_METER_READING_CODES>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, isFirst) : specification.and((Specification<M_METER_READING_CODES>) PagingUtils.createCostCenterFilterForCostCenterIdColumnName(specification, ccList, isFirst));
        }
        return specification;
    }
    
    Optional<M_METER_READING_CODES> findByCostCenterIdAndCodeIgnoreCase(Integer costCenterId, String code);

    List<M_METER_READING_CODES> findByCostCenterId (Integer ccId);
    
    Optional<M_METER_READING_CODES> findByMeterReadingCodeId(Integer meterReadingCodeId);
    
    List<M_METER_READING_CODES> findAll();
    
    List<M_METER_READING_CODES> findAllByCode(String code);
    List<M_METER_READING_CODES> findAllByStatus(String status);
    List<M_METER_READING_CODES> findAllByStatusAndCostCenterId(String status, Integer ccId);

    Boolean existsByCodeIgnoreCaseAndCostCenterId(String code, Integer costCenterId);
}

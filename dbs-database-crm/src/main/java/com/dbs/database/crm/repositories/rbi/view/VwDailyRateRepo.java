package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_DAILY_RATE;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface VwDailyRateRepo extends PagingAndSortingRepository<VW_DAILY_RATE, Integer>, JpaSpecificationExecutor<VW_DAILY_RATE> {
    default Specification<VW_DAILY_RATE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_DAILY_RATE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_DAILY_RATE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_DAILY_RATE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_DAILY_RATE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_DAILY_RATE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_DAILY_RATE> addDefaultFilters(Specification<VW_DAILY_RATE> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<VW_DAILY_RATE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_DAILY_RATE>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        return specification;
    }

//    @Query(nativeQuery = true, value = "SELECT * FROM VW_DAILY_RATE vdr \n" +
//            "WHERE vdr.RATE_DATE = TO_DATE(:rateDate, 'dd MMM yyyy')\n" +
//            "AND VDR.STATUS = :status")
    public List<VW_DAILY_RATE> findAllByRateDateAndStatus(Date rateDate, String status);
    public List<VW_DAILY_RATE> findAllByRateDateAndStatusAndRateTypeAndFromCurrency(String rateDate, String status, String rateType,Integer currencyId);
    public List<VW_DAILY_RATE> findAllByFromCurrencyAndRateDateAndStatus(Integer fromCurrency, String rateDate, String status);
    @Override
    List<VW_DAILY_RATE> findAll();

    List<VW_DAILY_RATE> findAllByStatusAndRateTypeAndRateDate(String status, String rateType, Date rateDate);
    @Query(nativeQuery = true, value = "SELECT * FROM VW_DAILY_RATE vdr \n" +
            "WHERE VDR.RATE_DATE = TO_DATE(:rateDate, 'yyyy-MM-dd')\n" +
            "AND STATUS = 'ACTIVE'\n" +
            "AND RATE_TYPE = :rateType\n" +
            "AND FROM_CURRENCY = :currencyId")
    List<VW_DAILY_RATE> findAllRateDate (String rateDate, String rateType, Integer currencyId);
}

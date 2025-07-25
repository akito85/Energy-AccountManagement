package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_DAILY_RATES;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiDailyRatesRepo extends PagingAndSortingRepository<M_RBI_DAILY_RATES, Integer>, JpaSpecificationExecutor<M_RBI_DAILY_RATES> {
    default Specification<M_RBI_DAILY_RATES> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_DAILY_RATES> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_DAILY_RATES>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_DAILY_RATES>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_DAILY_RATES> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_DAILY_RATES> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_DAILY_RATES> addDefaultFilters(Specification<M_RBI_DAILY_RATES> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<M_RBI_DAILY_RATES>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        return specification;
    }

    public List<M_RBI_DAILY_RATES> findAllByRateDateAndStatus(Date rateDate, String status);
    @Override
    public List<M_RBI_DAILY_RATES> findAll();

    Optional<M_RBI_DAILY_RATES> findByStatusAndFromCurrencyAndRateDateBetweenAndRateType(String status, int fromCurrency, Date startRateDate, Date endRateDate, String rateType);
    List<M_RBI_DAILY_RATES> findAllByRateTypeAndFromCurrencyAndToCurrencyAndRateDate(String rateType,Integer fromCurrency,Integer toCurrency, Date rateDate);

    Optional<M_RBI_DAILY_RATES> findByStatusAndFromCurrencyAndToCurrencyAndRateDateAndRateType(String status, int fromCurrency, int toCurrency, Date rateDate, String rateType);

    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_DAILY_RATES\n" +
            "WHERE RATE_DATE = TO_DATE(:endDatePeriod , 'yyyy-MM-dd HH24:MI:SS') \n" +
            "AND RATE_TYPE = :rateType \n" +
            "AND STATUS = 'ACTIVE'")
    List<M_RBI_DAILY_RATES> findByRateDate (String endDatePeriod, String rateType);
}

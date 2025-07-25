package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CUSTOMER;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_RATING;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
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
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiRatingRepo extends PagingAndSortingRepository<M_RBI_RATING,String>, JpaSpecificationExecutor<M_RBI_RATING> {
    default Specification<M_RBI_RATING> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_RBI_RATING> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_RATING>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_RATING>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_RBI_RATING> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_RATING> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_RBI_RATING> addDefaultFilters(Specification<M_RBI_RATING> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("serviceType") != null) {
            specification = (Specification<M_RBI_RATING>) PagingUtils.createSpecification("serviceType~" + (filter.get("serviceType")),EQUALS_SELECTOR);
        }

        return specification;
    }
    List<M_RBI_RATING> findAll();
    List<M_RBI_RATING> findAllByRatingCode(String ratingCode);
    Optional<M_RBI_RATING> findByAccountNumberAndRatingCodeAndBillingPeriod(String accNumb, String ratingCode, Date billPeriod);
    Optional<M_RBI_RATING> findByRatingCode(String ratingCode);

    @Query(nativeQuery = true, value = "SELECT * FROM M_RBI_RATING RR\n" +
            "LEFT JOIN M_RBI_PERIOD RP ON RP.PERIOD = RR.BILLING_PERIOD\n" +
            "WHERE RR.ACCOUNT_NUMBER = :accNumb ")
    List<M_RBI_RATING> findAllByAccountNumber(String accNumb);
}

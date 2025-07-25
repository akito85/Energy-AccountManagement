package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_CALC_SPECIFIC_CUSTOMER;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

public interface RRbiCalcSpecCustRepo extends PagingAndSortingRepository<R_RBI_CALC_SPECIFIC_CUSTOMER, Integer>, JpaSpecificationExecutor<R_RBI_CALC_SPECIFIC_CUSTOMER> {
    default Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_RBI_CALC_SPECIFIC_CUSTOMER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_RBI_CALC_SPECIFIC_CUSTOMER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> addDefaultFilters(Specification<R_RBI_CALC_SPECIFIC_CUSTOMER> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
//        specification = (Specification<R_RBI_CALCULATION_ACCOUNT_GROUP_TYPE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    List<R_RBI_CALC_SPECIFIC_CUSTOMER> findAllByCalCode (String calCode);

    @Query(nativeQuery = true ,value = "SELECT SA.SA_NUMBER FROM M_RBI_CALCULATION_JOB CJ\n" +
            "INNER JOIN R_RBI_CALC_SPECIFIC_CUSTOMER SC ON SC.CALCULATION_CODE = CJ.CALCULATION_CODE\n" +
            "INNER JOIN VW_ACCOUNT_INFORMATION AI ON AI.ACCOUNT_NUMBER = SC.CUSTOMER_NUMBER\n" +
            "LEFT JOIN VW_SA SA ON SA.ACCOUNT_ID = AI.ACCOUNT_ID\n" +
            "WHERE SA.SA_TYPE = 'PJBG' AND SA.STATUS = 'ACTIVE' AND AI.ACCOUNT_NUMBER = :custNumb AND SA.IS_MAIN = 'Y' AND SC.CALCULATION_CODE = :calCode")
    String findSaNumb (String custNumb, String calCode);
}

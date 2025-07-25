package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT;
import com.dbs.database.crm.entities.ratingbillinginvoice.TEMP_RBI_USAGE;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_CALC_RESULT;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
import com.dbs.database.crm.entities.usermanagement.view.VW_CC_SIBLINGS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;

import java.util.Optional;

import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAccountRepo extends PagingAndSortingRepository<VW_ACCOUNT, Integer>, JpaSpecificationExecutor<VW_ACCOUNT> {
    default Specification<VW_ACCOUNT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_ACCOUNT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ACCOUNT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT> addDefaultFilters(Specification<VW_ACCOUNT> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    default Specification<VW_ACCOUNT> getSpecificationFromFilters2(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters2(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_ACCOUNT> getSpecificationDefault2(Map<String, Object> filter) {
        Specification<VW_ACCOUNT> specification = null;
        specification = addDefaultFilters2(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT> addDefaultFilters2(Specification<VW_ACCOUNT> specification, Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_ACCOUNT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        specification = specification.and((Specification<VW_ACCOUNT>) PagingUtils.createCustomerFilter(specification, String.valueOf(Integer.parseInt(filter.get("customerId").toString())), Boolean.FALSE));
        return specification;
    }

    List<VW_ACCOUNT> findAllByCustomerId(Integer customerId);

    Optional<VW_ACCOUNT> findByAccountId(Integer accountId);

    Optional<VW_ACCOUNT> findByAccountNumber(String accNumb);
    
    List<VW_ACCOUNT> findAll();

    List<VW_ACCOUNT> findAllByCustomerIdAndCostCenterId(Integer customerId, Integer costCenterId);

    List<VW_ACCOUNT> findAllByCustomerIdAndCustomerManagementId(Integer customerId, Integer customerManagementId);
}

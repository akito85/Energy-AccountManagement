package com.dbs.database.crm.repositories.report;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.report.VW_REPORT_AGREEMENT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwReportAgreementRepo extends PagingAndSortingRepository<VW_REPORT_AGREEMENT, Integer>, JpaSpecificationExecutor<VW_REPORT_AGREEMENT> {
    default Specification<VW_REPORT_AGREEMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_REPORT_AGREEMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_REPORT_AGREEMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_REPORT_AGREEMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_REPORT_AGREEMENT> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<VW_REPORT_AGREEMENT> addDefaultFilters(Specification<VW_REPORT_AGREEMENT> specification, Map<String, Object> filter, Boolean isFirst) {
//        Object accIdObj = filter.get("accountId");
//        if (accIdObj != null) {
//            int accountId = Integer.parseInt(accIdObj.toString());
//            specification = (Specification<VW_REPORT_AGREEMENT>) PagingUtils.createAccountIdFilter(specification, accountId, isFirst);
//
//            specification = specification == null
//                    ? (Specification<VW_REPORT_AGREEMENT>) PagingUtils.createSpecification("accountId~" + accountId, DEFAULT_SELECTOR)
//                    : specification.and((Specification<VW_REPORT_AGREEMENT>) PagingUtils.createSpecification("accountId~" + accountId, DEFAULT_SELECTOR));
//        }
        return specification;
    }
    Optional<VW_REPORT_AGREEMENT> findById(Integer id);

    @Override
    public List<VW_REPORT_AGREEMENT> findAll();
}

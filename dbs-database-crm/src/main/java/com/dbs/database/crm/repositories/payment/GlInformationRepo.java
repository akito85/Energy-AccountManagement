package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_GL_INFORMATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface GlInformationRepo extends PagingAndSortingRepository<R_GL_INFORMATION, Long>, JpaSpecificationExecutor<R_GL_INFORMATION> {

    @SuppressWarnings("unchecked")
    default Specification<R_GL_INFORMATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_GL_INFORMATION> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_GL_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_GL_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_GL_INFORMATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_GL_INFORMATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_GL_INFORMATION> addDefaultFilters(Specification<R_GL_INFORMATION> specification,
                                                                Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<R_GL_INFORMATION>) PagingUtils
                .createPaymentItemIdFilter(specification, Long.parseLong(filter.get("paymentItemId").toString()), isFirst);
        return specification;
    }
}

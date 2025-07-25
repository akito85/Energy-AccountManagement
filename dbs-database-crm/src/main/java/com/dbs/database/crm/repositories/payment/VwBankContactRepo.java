package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_BANK_CONTACT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwBankContactRepo extends PagingAndSortingRepository<VW_BANK_CONTACT, Long>, JpaSpecificationExecutor<VW_BANK_CONTACT> {

    @SuppressWarnings("unchecked")
    default Specification<VW_BANK_CONTACT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_BANK_CONTACT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_BANK_CONTACT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_BANK_CONTACT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_BANK_CONTACT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_BANK_CONTACT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_BANK_CONTACT> addDefaultFilters(Specification<VW_BANK_CONTACT> specification,
                                                                              Map<String, Object> filter, Boolean isFirst) {
        if(!ObjectUtils.isEmpty(filter.get("bankCode"))) {
            specification = isFirst.equals(Boolean.TRUE) ? (Specification<VW_BANK_CONTACT>) where(PagingUtils.createSpecification("bankCode~"+filter.get("bankCode").toString(), DEFAULT_SELECTOR)) : specification.and((Specification<VW_BANK_CONTACT>) PagingUtils.createSpecification("bankCode~"+filter.get("bankCode").toString(), DEFAULT_SELECTOR));
        }
        return specification;
    }
}

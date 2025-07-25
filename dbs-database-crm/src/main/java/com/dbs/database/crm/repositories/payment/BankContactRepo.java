package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_PAY_BANK_CONTACT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface BankContactRepo extends PagingAndSortingRepository<R_PAY_BANK_CONTACT, Long>, JpaSpecificationExecutor<R_PAY_BANK_CONTACT> {

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_BANK_CONTACT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_PAY_BANK_CONTACT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PAY_BANK_CONTACT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PAY_BANK_CONTACT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_PAY_BANK_CONTACT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PAY_BANK_CONTACT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_BANK_CONTACT> addDefaultFilters(Specification<R_PAY_BANK_CONTACT> specification,
                                                                      Map<String, Object> filter, Boolean isFirst) {
        if (!ObjectUtils.isEmpty(filter.get("bankId")))
            specification = (Specification<R_PAY_BANK_CONTACT>) PagingUtils
                    .createCommonColumnNumberEqualsFilter(specification, "bankId", Long.parseLong(filter.get("bankId").toString()), isFirst);

        if (!ObjectUtils.isEmpty(filter.get("bankCode")))
            specification = (Specification<R_PAY_BANK_CONTACT>) PagingUtils
                    .createCommonColumnVarcharEqualsFilter(specification, "bankCode", filter.get("bankCode").toString(), isFirst);
        return specification;
    }

    List<R_PAY_BANK_CONTACT> findAllByBankId(Long bankId);
    List<R_PAY_BANK_CONTACT> findAllByBankIdAndPrimaryFlag(Long bankId, Boolean primaryFlag);

    List<R_PAY_BANK_CONTACT> findAllByBankCode(String bankCode);
}

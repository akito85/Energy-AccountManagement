package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_PAY_ACCOUNT_BANK_CRITERIA_DATA;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface AccountBankCriteriaDataRepo extends PagingAndSortingRepository<R_PAY_ACCOUNT_BANK_CRITERIA_DATA, Long>, JpaSpecificationExecutor<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> {

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> addDefaultFilters(Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> specification,
                                                                       Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<R_PAY_ACCOUNT_BANK_CRITERIA_DATA>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "accountInformationId", Long.parseLong(filter.get("accountInformationId").toString()), isFirst);
        return specification;
    }

    void deleteAllByIdNotInAndAccountInformationId(List<Long> idList, Long accountInformationId);

    List<R_PAY_ACCOUNT_BANK_CRITERIA_DATA> findAllByAccountInformationId(Long accountInformationId);
}

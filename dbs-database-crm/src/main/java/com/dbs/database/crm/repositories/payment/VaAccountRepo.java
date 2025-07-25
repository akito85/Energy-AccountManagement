package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.R_PAY_VA_ACCOUNT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VaAccountRepo extends PagingAndSortingRepository<R_PAY_VA_ACCOUNT, Long>, JpaSpecificationExecutor<R_PAY_VA_ACCOUNT> {

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_VA_ACCOUNT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<R_PAY_VA_ACCOUNT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_PAY_VA_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_PAY_VA_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<R_PAY_VA_ACCOUNT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_PAY_VA_ACCOUNT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<R_PAY_VA_ACCOUNT> addDefaultFilters(Specification<R_PAY_VA_ACCOUNT> specification,
                                                                      Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<R_PAY_VA_ACCOUNT>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "bankAccountId", Long.parseLong(filter.get("bankAccountId").toString()), isFirst);
        return specification;
    }

    Optional<R_PAY_VA_ACCOUNT> findByVaNumber(String vaNumber);

    @Query(value = "SELECT a.* FROM \n" +
            "\t(\n" +
            "\t\tSELECT ACCOUNT_NUMBER FROM R_PAY_VA_ACCOUNT WHERE :vaNumber LIKE '%' || VA_NUMBER || '%'\n" +
            "\t) a\n" +
            "INNER JOIN M_ACCOUNT b ON a.ACCOUNT_NUMBER = b.ACCOUNT_NUMBER \n" +
            "INNER JOIN (SELECT ACCOUNT_NUMBER FROM M_RBI_BILLING GROUP BY ACCOUNT_NUMBER) c ON a.ACCOUNT_NUMBER = c.ACCOUNT_NUMBER", nativeQuery = true)
    Optional<String> findAccountNumberByVaNumber(String vaNumber);
}

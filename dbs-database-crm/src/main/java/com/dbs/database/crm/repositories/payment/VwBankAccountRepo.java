package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.view.VW_PAY_BANK_ACCOUNT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwBankAccountRepo extends PagingAndSortingRepository<VW_PAY_BANK_ACCOUNT, Long>, JpaSpecificationExecutor<VW_PAY_BANK_ACCOUNT> {

    @SuppressWarnings("unchecked")
    default Specification<VW_PAY_BANK_ACCOUNT> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_PAY_BANK_ACCOUNT> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            String[] searchs = sr.trim().split("~");
            specification =
                    i == 0 ?
                            (Specification<VW_PAY_BANK_ACCOUNT>) where(PagingUtils.createSpecification(searchs[0],searchs[1],DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_PAY_BANK_ACCOUNT>) PagingUtils.createSpecification(searchs[0],searchs[1],DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_PAY_BANK_ACCOUNT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PAY_BANK_ACCOUNT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_PAY_BANK_ACCOUNT> addDefaultFilters(Specification<VW_PAY_BANK_ACCOUNT> specification,
                                                                       Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<VW_PAY_BANK_ACCOUNT>) PagingUtils
                .createCommonColumnNumberEqualsFilter(specification, "bankId", Long.parseLong(filter.get("bankId").toString()), isFirst);
        return specification;
    }

    @Query(value = "SELECT \n" +
            "\ta.ID,\n" +
            "    b.BANK_NAME || ' - ' || a.CURRENCY || ' - ' || a.ACCOUNT_NUMBER BANK_ACCOUNT\n" +
            "FROM VW_PAY_BANK_ACCOUNT a\n" +
            "INNER JOIN M_PAY_BANK b ON a.BANK_ID = b.BANK_ID\n" +
            "UNION \n" +
            "SELECT 0 ID, 'All - Bank' BANK_ACCOUNT FROM dual", nativeQuery = true)
    List<Object[]> findAllDropdownBankAccount();

    @Query(value = "SELECT \n" +
            "b.BANK_NAME || ' - ' || a.CURRENCY || ' - ' || a.ACCOUNT_NUMBER BANK_ACCOUNT\n" +
            "FROM \n" +
            "(\n" +
            "SELECT ID, BANK_ID, CURRENCY, ACCOUNT_NUMBER \n" +
            "FROM VW_PAY_BANK_ACCOUNT\n" +
            "WHERE ID =:id\n" +
            ") a\n" +
            "INNER JOIN M_PAY_BANK b ON a.BANK_ID = b.BANK_ID", nativeQuery = true)
    String findOneById(Long id);
}

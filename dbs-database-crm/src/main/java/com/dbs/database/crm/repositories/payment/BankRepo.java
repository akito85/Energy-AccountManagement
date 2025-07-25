package com.dbs.database.crm.repositories.payment;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.payment.M_PAY_BANK;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface BankRepo extends PagingAndSortingRepository<M_PAY_BANK, Long>, JpaSpecificationExecutor<M_PAY_BANK> {

    @SuppressWarnings("unchecked")
    default Specification<M_PAY_BANK> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_PAY_BANK> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            String[] searchs = sr.trim().split("~");
            if (searchs.length > 0 && searchs[0].equals("npwp")) {
                sr = "npwp~"+searchs[1].replace(".", "").replace("-", "");
            }
            specification =
                    i == 0 ?
                            (Specification<M_PAY_BANK>) where(PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PAY_BANK>) PagingUtils.createSpecificationPayment(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_PAY_BANK> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PAY_BANK> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PAY_BANK> addDefaultFilters(Specification<M_PAY_BANK> specification,
                                                        Map<String, Object> filter, Boolean isFirst) {
        return specification;
    }

    Optional<M_PAY_BANK> findByIdAndStatusApproval(Long id, String statusApproval);

    @Query(value = "SELECT BANK_CODE, BANK_NAME, BANK_SHORT_NAME, BANK_CODE || ' - ' || UPPER(BANK_NAME) BANK_CODE_NAME FROM M_PAY_BANK WHERE IS_BRANCH = 'N' AND STATUS='Active' ORDER BY BANK_CODE", nativeQuery = true)
    List<Tuple> findBankNotBranch();

    List<M_PAY_BANK> findAll();

    @Query(value = "SELECT 1 FROM M_PAY_BANK WHERE (BANK_NAME COLLATE BINARY_CI =:bankName OR BANK_SHORT_NAME COLLATE BINARY_CI =:bankShortName) AND IS_BRANCH='N'", nativeQuery = true)
    Optional<Integer> findByBankNameOrBankShortName(String bankName, String bankShortName);

    @Query(value = "SELECT 1 FROM M_PAY_BANK WHERE (BANK_NAME COLLATE BINARY_CI =:bankName OR BANK_SHORT_NAME COLLATE BINARY_CI =:bankShortName) AND BANK_ID <>:bankId AND IS_BRANCH='N'", nativeQuery = true)
    Optional<Integer> findByBankIdBankNameOrBankShortName(String bankName, String bankShortName, Long bankId);

    List<M_PAY_BANK> findAllByStatusAndStatusApprovalAndIsBranch(String status, String statusApproval, boolean isBranch);

    List<M_PAY_BANK> findAllByStatusIgnoreCaseAndIsBranch(String status, boolean isBranch);

    boolean existsByNpwp(String npwp);

    boolean existsByIdNotAndNpwp(Long id, String npwp);

    boolean existsByBankCodeIgnoreCaseAndIsBranch(String bankCode, boolean isBranch);

    boolean existsByIdNotAndBankCodeIgnoreCaseAndIsBranch(Long id, String bankCode, boolean isBranch);

    boolean existsByBranchNameIgnoreCaseAndBankShortNameIgnoreCaseAndIsBranch(String branchName, String bankShortName, boolean isBranch);

    boolean existsByIdNotAndBranchNameIgnoreCaseAndBankShortNameIgnoreCaseAndIsBranch(Long id, String branchName, String bankShortName, boolean isBranch);

    boolean existsByBankNameIgnoreCaseAndIsBranch(String bankName, boolean isBranch);

    boolean existsByBankShortNameIgnoreCaseAndIsBranch(String bankShortName, boolean isBranch);

    boolean existsByIdNotAndBankNameIgnoreCaseAndIsBranch(Long id, String bankName, boolean isBranch);

    boolean existsByIdNotAndBankShortNameIgnoreCaseAndIsBranch(Long id, String bankShortName, boolean isBranch);
}

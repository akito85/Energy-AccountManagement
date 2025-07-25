package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface MAccountRepo extends PagingAndSortingRepository<M_ACCOUNT, Integer>, JpaSpecificationExecutor<M_ACCOUNT> {
    
    default Specification<M_ACCOUNT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ACCOUNT> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                    (Specification<M_ACCOUNT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                    : specification.and((Specification<M_ACCOUNT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }
    
    default Specification<M_ACCOUNT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ACCOUNT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_ACCOUNT> addDefaultFilters(Specification<M_ACCOUNT> specification, Map<String, Object> filter, Boolean isFirst) {
        if (!ObjectUtils.isEmpty(filter.get("accountNumber")))
            specification = (Specification<M_ACCOUNT>) PagingUtils
                    .createCommonColumnLikeFilter(specification, "accountNumber", filter.get("accountNumber").toString(), isFirst);

        if (!ObjectUtils.isEmpty(filter.get("accountName")))
            specification = (Specification<M_ACCOUNT>) PagingUtils
                    .createCommonColumnLikeFilter(specification, "accountName", filter.get("accountName").toString(), isFirst);
        return specification;
    }
    
    @Query("SELECT a FROM M_ACCOUNT a WHERE a.id NOT IN :accountId")
    List<M_ACCOUNT> findAllNotIn(Integer accountId);
    
    List<M_ACCOUNT> findAll();
    
    Optional<M_ACCOUNT> findByAccountId(Integer accountId);

    List<M_ACCOUNT> findByCustomerId(Integer customerId);

    List<M_ACCOUNT> findByCostCenterAndMeterReadingCodeAndSorAndAccountGroupTypeAndAccountSegment (int ccId, int mRc, int sor, int accGroup, int accSegment);

    @Query(value = "select ma.* from m_account ma where ma.account_number=:accountNumber", nativeQuery = true)
    Optional<M_ACCOUNT> findByAccountNumber(String accountNumber);
    List<M_ACCOUNT> findAllByCustomerId(Integer customerId);
    List<M_ACCOUNT> findTopByOrderByAccountIdDesc();
    Optional<M_ACCOUNT> findByAccountNumberAndAccountGroup(String accountNumber, String accountGroup);
    
    Optional<M_ACCOUNT> findTopByRegistrationNumber(String registrationNumber);
    List<M_ACCOUNT> findAllByMeterReadingCode(Integer meterReadingCode);
    
    Boolean existsByRegistrationNumber(String registrationNumber);

    @Query(value = "select * from m_account where :accountNumber like '%' || account_number || '%'", nativeQuery = true)
    Optional<M_ACCOUNT> findByAccountNumberLike(String accountNumber);

    List<M_ACCOUNT> findAllByStatus(String status);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(ACCOUNT_NUMBER)),0) FROM M_ACCOUNT", nativeQuery = true)
    Integer findMaxCode();
}


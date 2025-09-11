package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.accountmanagement.T_AM_SA;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TAmSARepo extends PagingAndSortingRepository<T_AM_SA, Integer>, JpaSpecificationExecutor<T_AM_SA> {

    default Specification<T_AM_SA> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<T_AM_SA> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_AM_SA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_AM_SA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<T_AM_SA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<T_AM_SA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<T_AM_SA> addDefaultFilters(Specification<T_AM_SA> specification, Map<String, Object> filter, Boolean isFirst) {

        if(filter.get("accountId") != null) {
            specification = (Specification<T_AM_SA>) PagingUtils.createSpecification("accountId~" + (filter.get("accountId")),DEFAULT_SELECTOR);
        }

        return specification;
    }
//    Optional<T_AM_SA> findByAccountId(Integer accountId);

    List<T_AM_SA> findAllByAccountId(Integer accountId);

    List<T_AM_SA> findAllByAccountIdAndStatus(Integer accountId, String status);

    Optional<T_AM_SA> findBySaNumber(String saNumber);

    List<T_AM_SA> findAllByAccountIdAndSaServiceTypeAndStatus(Integer accountId, Integer serviceType, String status);
    Optional<T_AM_SA> findTopByAccountIdAndSaServiceTypeAndIsMainAndApprovalStatusInOrderByIdAsc(Integer accountId, Integer serviceType, String isMain, List<String> status);
    Optional<T_AM_SA> findTopByAccountIdAndSaServiceTypeAndIsMainAndApprovalStatusInAndIdNotOrderByIdAsc(Integer accountId, Integer serviceType, String isMain, List<String> status, Integer id);

    @Query(value = "select tas.* from T_AM_SA tas where tas.ACCOUNT_ID=:accountId and tas.STATUS=:status and tas.IS_MAIN=:isMain", nativeQuery = true)
    Optional<T_AM_SA> findByAccountIdAndStatusAndIsMain(Integer accountId, String status, String isMain);
    
    Optional<T_AM_SA> findBySaNumberAndStatus(String saNumber,String status);
    
}




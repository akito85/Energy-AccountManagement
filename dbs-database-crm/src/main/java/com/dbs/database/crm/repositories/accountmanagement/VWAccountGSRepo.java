package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT_GS;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAccountGSRepo extends PagingAndSortingRepository<VW_ACCOUNT_GS, Integer>, JpaSpecificationExecutor<VW_ACCOUNT_GS> {
    default Specification<VW_ACCOUNT_GS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT_GS> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT_GS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT_GS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_ACCOUNT_GS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ACCOUNT_GS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT_GS> addDefaultFilters(Specification<VW_ACCOUNT_GS> specification, Map<String, Object> filter, Boolean isFirst) {
        if(isFirst){
            specification = (Specification<VW_ACCOUNT_GS>) where(PagingUtils.createSpecification("accountId~"+filter.get("accountId").toString(), EQUALS_SELECTOR));
        }else{
            specification = specification.and((Specification<VW_ACCOUNT_GS>) PagingUtils.createSpecification("accountId~"+filter.get("accountId").toString(), EQUALS_SELECTOR));
        }
        return specification;
    }

    List<VW_ACCOUNT_GS> findAllByEndDate(String endDate);

    Optional<VW_ACCOUNT_GS> findTopByAccountIdAndStatusOrderByCreatedDateDesc(Integer accountId, String status);
}

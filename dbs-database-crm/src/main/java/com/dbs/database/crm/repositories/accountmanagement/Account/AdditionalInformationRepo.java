package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_ADDITIONAL_INFORMATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.spec.OAEPParameterSpec;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface AdditionalInformationRepo extends PagingAndSortingRepository<M_AM_ADDITIONAL_INFORMATION, Integer>, JpaSpecificationExecutor<M_AM_ADDITIONAL_INFORMATION> {

    default Specification<M_AM_ADDITIONAL_INFORMATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_ADDITIONAL_INFORMATION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_ADDITIONAL_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_ADDITIONAL_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_AM_ADDITIONAL_INFORMATION> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<M_AM_ADDITIONAL_INFORMATION> addDefaultFilters(Specification<M_AM_ADDITIONAL_INFORMATION> specification, Boolean isFirst){
        return specification;
    }

    Optional<M_AM_ADDITIONAL_INFORMATION> findByInformationTypeAndAccountIdAndIsDeleted (Integer id, Integer accId, Boolean isDel);

    List<M_AM_ADDITIONAL_INFORMATION> findAllByAccountIdAndIsDeleted(Integer id, Boolean isDel);
}

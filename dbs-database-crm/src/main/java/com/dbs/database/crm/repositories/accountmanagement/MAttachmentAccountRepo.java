package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
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
public interface MAttachmentAccountRepo extends PagingAndSortingRepository<M_ATTACHMENT, String>, JpaSpecificationExecutor<M_ATTACHMENT> {
    default Specification<M_ATTACHMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ATTACHMENT> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ATTACHMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ATTACHMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_ATTACHMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ATTACHMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ATTACHMENT> addDefaultFilters(Specification<M_ATTACHMENT> specification, Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("id") != null) {
            List<Integer> id = (List<Integer>) filter.get("id");
            specification = (Specification<M_ATTACHMENT>) PagingUtils.createIdFilter(specification, id, true);
        }

        if(filter.get("referenceId") != null && filter.get("category") != null){
            specification = (Specification<M_ATTACHMENT>) where(PagingUtils.createSpecification("referenceId"+"~"+filter.get("referenceId"), EQUALS_SELECTOR));
            specification = specification.and((Specification<M_ATTACHMENT>) PagingUtils.createSpecification("category"+"~"+filter.get("category"), DEFAULT_SELECTOR));
        }

        return specification;
    }

    Optional<M_ATTACHMENT> findById(Integer id);
    
    List<M_ATTACHMENT> findAllByReferenceId(Integer referenceId);
}

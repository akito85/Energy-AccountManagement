package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.LOG_EMAIL;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LogEmailRepo extends PagingAndSortingRepository <LOG_EMAIL, Integer>, JpaSpecificationExecutor<LOG_EMAIL>, JpaRepository<LOG_EMAIL, Integer> {

    default Specification<LOG_EMAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<LOG_EMAIL> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<LOG_EMAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<LOG_EMAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<LOG_EMAIL> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }


    List<LOG_EMAIL>findAllByStatus(String status);
    
}

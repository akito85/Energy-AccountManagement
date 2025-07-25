package com.dbs.database.crm.repositories.usermanagement.view;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_LOGIN_BACKGROUND;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWLoginBgRepo extends PagingAndSortingRepository<VW_LOGIN_BACKGROUND, Integer>, JpaSpecificationExecutor<VW_LOGIN_BACKGROUND> {
    
    default Specification<VW_LOGIN_BACKGROUND> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_LOGIN_BACKGROUND> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_LOGIN_BACKGROUND>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_LOGIN_BACKGROUND>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<VW_LOGIN_BACKGROUND> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }
    List<VW_LOGIN_BACKGROUND> findAll();
}

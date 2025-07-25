package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_LOGIN_BACKGROUND;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TAX_CODE;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MBackgroundRepo extends PagingAndSortingRepository<M_LOGIN_BACKGROUND, Integer>, JpaSpecificationExecutor<M_LOGIN_BACKGROUND> {

    default Specification<M_LOGIN_BACKGROUND> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_LOGIN_BACKGROUND> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_LOGIN_BACKGROUND>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_LOGIN_BACKGROUND>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<M_LOGIN_BACKGROUND> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<M_LOGIN_BACKGROUND> addDefaultFilters(Specification<M_LOGIN_BACKGROUND> specification, Boolean isFirst){
        return specification;
    }
    
    @Query("SELECT u FROM M_LOGIN_BACKGROUND u WHERE LOWER(u.backgroundName) = LOWER(:backgroundName)")
    Optional<M_LOGIN_BACKGROUND> findByBackgroundName(@Param("backgroundName") String backgroundName);

    List<M_LOGIN_BACKGROUND> findAll();

    Optional<M_LOGIN_BACKGROUND> findByLoginBackgroundId(Integer loginBackgroundId);
}

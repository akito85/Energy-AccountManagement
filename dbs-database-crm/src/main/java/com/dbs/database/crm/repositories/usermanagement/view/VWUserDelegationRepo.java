package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_USER_DELEGATION;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.view.VW_M_GLOBAL_TYPE;
import org.apache.commons.lang3.StringUtils;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
public interface VWUserDelegationRepo extends PagingAndSortingRepository<VW_USER_DELEGATION, Integer>, JpaSpecificationExecutor<VW_USER_DELEGATION> {
    default Specification<VW_USER_DELEGATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<VW_USER_DELEGATION> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_USER_DELEGATION>) where(PagingUtils.createSpecification(sr,DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_USER_DELEGATION>) PagingUtils.createSpecification(sr,DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<VW_USER_DELEGATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_USER_DELEGATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<VW_USER_DELEGATION> addDefaultFilters(Specification<VW_USER_DELEGATION> specification,
                                                               Map<String, Object> filter, Boolean isFirst) {
        if (filter.get("delegateFromId") != "") {
            specification = (Specification<VW_USER_DELEGATION>) PagingUtils.createHeaderFilter(specification, "delegateFromId", Integer.parseInt(filter.get("delegateFromId").toString()), isFirst);
        }
        return specification;
    }

    @Query(value="SELECT a.* FROM VW_USER_DELEGATION a WHERE (a.DELEGATE_FROM_ID = :employeeId OR a.DELEGATE_TO_ID= :employeeId) AND a.STATUS = 'ACTIVE' AND a.START_DATE<=SYSDATE AND a.END_DATE>=SYSDATE", nativeQuery = true)
    List<VW_USER_DELEGATION> findingByDelegateFromIdorDelegateToIdAndStatus(Integer employeeId);

    @Query(value="SELECT a.* FROM VW_USER_DELEGATION a \n" +
            "WHERE a.DELEGATE_FROM_ID = :delegateFrom \n" +
            "AND a.POSITION_FROM_DELEGATOR_ID = :position \n" +
            "AND a.status = :status AND\n" +
            "a.START_DATE<=SYSDATE AND\n" +
            "a.END_DATE >= SYSDATE", nativeQuery = true)
    List<VW_USER_DELEGATION> findByDelegateFrom(Integer delegateFrom, String status, Integer position);
}

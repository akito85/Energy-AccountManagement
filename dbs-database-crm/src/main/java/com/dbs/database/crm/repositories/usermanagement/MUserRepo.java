package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MUserRepo extends PagingAndSortingRepository<M_USER, Integer>, JpaSpecificationExecutor<M_USER> {
    default Specification<M_USER> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_USER> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_USER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_USER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_USER> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_USER> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_USER> addDefaultFilters(Specification<M_USER> specification,
                                                    Map<String, Object> filter, Boolean isFirst) {
        specification = (Specification<M_USER>) PagingUtils.createEntityFilter(specification,
                Integer.parseInt(filter.get("entityId").toString()), isFirst);
        return specification;
    }

    @Query("SELECT u FROM M_USER u WHERE LOWER(u.username) = LOWER(:username)")
    Optional<M_USER> findByUsernameIgnoreCase(@Param("username") String username);

    Optional<M_USER> findByUsername(String username);

    Optional<M_USER> findByEmployeeId(Integer employeeId);

    Optional<M_USER> findByUserCode(String userCode);

    List<M_USER> findAll();

    M_USER findByEmail(String email);

    M_USER findByPhone(String phone);

    List<M_USER> findAllByPhone(String phone);

    List<M_USER> findTopByOrderByUserIdDesc();

    Optional<M_USER> findByUserId(Integer userId);
    
    @Query("SELECT u FROM M_USER u WHERE LOWER(u.email) = LOWER(:email)")
    Optional<List<M_USER>> findAllByEmail(String email);
    
    Optional<M_USER> findByUsernameAndStatus(String username, String status);
    
    Optional<List<M_USER>> findAllByEmployeeId(Integer employeeId);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(USER_CODE, 4, 7))),0) FROM M_USER", nativeQuery = true)
    Integer findMaxCode();
}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_EMPLOYEE;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.R_DATA_ACCESS_HIERARCHY;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MEmployeeRepo extends PagingAndSortingRepository<M_EMPLOYEE, Integer>, JpaSpecificationExecutor<M_EMPLOYEE> {

    default Specification<M_EMPLOYEE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_EMPLOYEE> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_EMPLOYEE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_EMPLOYEE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_EMPLOYEE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_EMPLOYEE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_EMPLOYEE> addDefaultFilters(Specification<M_EMPLOYEE> specification, Map<String, Object> filter, Boolean isFirst) {

        specification = (Specification<M_EMPLOYEE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        return specification;
    }

    List<M_EMPLOYEE> findAll();

    M_EMPLOYEE findByEmail(String email);
    
    @Query(value = "select c.NAME " +
    "from (select i.FIRST_NAME || ' ' || i.LAST_NAME as NAME, i.* \n" +
    "      from M_EMPLOYEE i \n" +
    "     ) c \n" +
    "where LOWER(c.NAME) = LOWER(:firstName) ||' '|| LOWER(:lastName)", nativeQuery = true)
    List<String> findFirstNameAndLastName(@Param("firstName") String firstName, @Param("lastName") String lastName);
    
    Optional<List<M_EMPLOYEE>> findAllByEmail(String email);

    List<M_EMPLOYEE> findTopByEmail(String email);

    M_EMPLOYEE findFirstByPhone(String phone);

    List<M_EMPLOYEE> findTopByPhone(String phone);

    M_EMPLOYEE findByEmpNumber(String empNumber);

    Optional<M_EMPLOYEE> findFirstByEmpNumber(String empNumber);

    Optional<M_EMPLOYEE> findByEmployeeCode(String employeeCode);

    Optional<M_EMPLOYEE> findByEmployeeId(Integer employeeId);

    List<M_EMPLOYEE> findTopByOrderByEmployeeIdDesc();

    M_EMPLOYEE findTopByEmployeeId(Integer employeeId);

    M_EMPLOYEE findTopByFullName(String fullName);

    List<M_EMPLOYEE> findAllByOrderByEmployeeIdAsc();
    
    List<M_EMPLOYEE> findAllByStatusAndEntityId(String status, Integer entityId);
    
    List<M_EMPLOYEE> findAllByStatus(String status);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(EMPLOYEE_CODE, 4, 7))),0) FROM M_EMPLOYEE", nativeQuery = true)
    Integer findMaxCode();
}

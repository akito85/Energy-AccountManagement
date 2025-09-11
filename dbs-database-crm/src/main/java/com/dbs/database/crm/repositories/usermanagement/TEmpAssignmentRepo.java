package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.T_EMP_ASSIGNMENT;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TEmpAssignmentRepo extends PagingAndSortingRepository<T_EMP_ASSIGNMENT, Integer>, JpaSpecificationExecutor<T_EMP_ASSIGNMENT> {
    List<T_EMP_ASSIGNMENT> findAllByEmployeeCodeAndStatus(String employeeCode, String status);

    default Specification<T_EMP_ASSIGNMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata) {
        Specification<T_EMP_ASSIGNMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<T_EMP_ASSIGNMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<T_EMP_ASSIGNMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        return specification;
    }

    default Specification<T_EMP_ASSIGNMENT> getSpecificationDefault() {
        return null;
    }

    default Specification<T_EMP_ASSIGNMENT> addDefaultFilters(Specification<T_EMP_ASSIGNMENT> specification, Boolean isFirst) {
        return specification;
    }

    List<T_EMP_ASSIGNMENT> findByEmployeeCode(String employeeCode);

    Optional<T_EMP_ASSIGNMENT> findByEmpAssignmentId(Integer empAssignmentId);

    List<T_EMP_ASSIGNMENT> findByEmpNumber(String empNumber);

    @Override
    List<T_EMP_ASSIGNMENT> findAll();

    T_EMP_ASSIGNMENT findByPositionId(Integer positionId);
    
    List<T_EMP_ASSIGNMENT> findAllByPositionId(Integer positionId);

    List<T_EMP_ASSIGNMENT> findByPositionIdAndStatus(Integer positionId, String status);

    @Query(nativeQuery = true ,value = "SELECT * FROM T_EMP_ASSIGNMENT tea \n" +
            "LEFT JOIN M_EMPLOYEE me ON tea.EMPLOYEE_CODE = me.EMPLOYEE_CODE\n" +
            "WHERE me.STATUS = 'ACTIVE' AND tea.STATUS = 'ACTIVE' \n" +
            "AND tea.END_DATE >= SYSDATE AND tea.POSITION_ID = :positionId" )
    List<T_EMP_ASSIGNMENT> findPositionId (Integer positionId);

    List<T_EMP_ASSIGNMENT> findAllByJobId(Integer jobId);
    
    @Query("SELECT a FROM T_EMP_ASSIGNMENT a WHERE a.empNumber = :empNumber AND a.empAssignmentId NOT IN :empAssignmentId")
    List<T_EMP_ASSIGNMENT> findNotIn(String empNumber, List<Integer> empAssignmentId);

}

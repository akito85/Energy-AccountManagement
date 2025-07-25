package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_EMPLOYEE_ASSIGNMENT;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWEmployeeAssignmentRepo extends PagingAndSortingRepository<VW_EMPLOYEE_ASSIGNMENT, Integer>, JpaSpecificationExecutor<VW_EMPLOYEE_ASSIGNMENT> {

    Optional<VW_EMPLOYEE_ASSIGNMENT> findFirstByPositionIdAndStatus(Integer parent, String status);

    List<VW_EMPLOYEE_ASSIGNMENT> findByPositionIdAndStatus(Integer parent, String status);
    List<VW_EMPLOYEE_ASSIGNMENT> findByPositionIdAndStatusAndEmployeeStatusAndEmployeeIdNot(Integer parent, String status, String employeeStatus, Integer employeeId);

    Optional<Set<VW_EMPLOYEE_ASSIGNMENT>> findAllByEmployeeCodeAndStatus(String empCode, String status);

    Optional<VW_EMPLOYEE_ASSIGNMENT> findFirstByEmployeeCode (String employeeCode);

    default Specification<VW_EMPLOYEE_ASSIGNMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_EMPLOYEE_ASSIGNMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_EMPLOYEE_ASSIGNMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_EMPLOYEE_ASSIGNMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_EMPLOYEE_ASSIGNMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_EMPLOYEE_ASSIGNMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_EMPLOYEE_ASSIGNMENT> addDefaultFilters(Specification<VW_EMPLOYEE_ASSIGNMENT> specification, Map<String, Object> filter, Boolean isFirst) {
        /*INFO: Position Filter*/
        specification = (Specification<VW_EMPLOYEE_ASSIGNMENT>) PagingUtils.createHeaderFilter(specification, "positionId", Integer.parseInt(filter.get("positionId").toString()), isFirst);

        return specification;
    }

    public List<VW_EMPLOYEE_ASSIGNMENT> findAllByEmployeeStatus(String employeeStatus);
    
    Optional<VW_EMPLOYEE_ASSIGNMENT> findFirstByEmployeeIdAndPositionIdAndStatus(Integer employeeId, Integer positionId, String status);
    
    @Query("SELECT a FROM VW_EMPLOYEE_ASSIGNMENT a WHERE a.positionId IN :positionId")
    List<VW_EMPLOYEE_ASSIGNMENT> findInByPositionId(List<Integer> positionId);
    
    
}


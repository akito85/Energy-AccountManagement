package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_ASSIGNMENT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAssignmentRepo extends PagingAndSortingRepository<VW_ASSIGNMENT, Integer>,JpaSpecificationExecutor<VW_ASSIGNMENT> {
    List<VW_ASSIGNMENT> findByEmployeeCodeAndStatus (String employeeCode, String status);
    List<VW_ASSIGNMENT> findByEmployeeCode(String employeeCode);
    List<VW_ASSIGNMENT> findAll();
}

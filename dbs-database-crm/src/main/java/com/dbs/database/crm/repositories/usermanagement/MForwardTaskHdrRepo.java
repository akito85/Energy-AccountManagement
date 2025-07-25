package com.dbs.database.crm.repositories.usermanagement;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.usermanagement.M_FORWARD_TASK_HDR;
import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MForwardTaskHdrRepo extends PagingAndSortingRepository<M_FORWARD_TASK_HDR, Integer>, JpaSpecificationExecutor<M_FORWARD_TASK_HDR> {

    public List<M_FORWARD_TASK_HDR> findAllByemployeeCode(String employeeCode);
    
}

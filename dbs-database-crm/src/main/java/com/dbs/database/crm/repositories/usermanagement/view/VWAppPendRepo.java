package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_APPROVAL_PENDING;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAppPendRepo extends PagingAndSortingRepository<VW_APPROVAL_PENDING, Integer>, JpaSpecificationExecutor<VW_APPROVAL_PENDING> {
    
    List<VW_APPROVAL_PENDING> findAllByPositionId(Integer positionId);
}

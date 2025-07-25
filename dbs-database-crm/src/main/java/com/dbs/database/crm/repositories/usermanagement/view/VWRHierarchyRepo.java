package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_R_DATA_ACCESS_HIERARCHY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWRHierarchyRepo extends PagingAndSortingRepository<VW_R_DATA_ACCESS_HIERARCHY,String> , JpaSpecificationExecutor<VW_R_DATA_ACCESS_HIERARCHY> {
}

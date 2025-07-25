package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_USER_GROUPACCESS;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWUserGroupAccRepo extends PagingAndSortingRepository<VW_USER_GROUPACCESS, Integer>, JpaSpecificationExecutor<VW_USER_GROUPACCESS> {
    Optional<List<VW_USER_GROUPACCESS>> findAllByGaId(Integer gaId);
}

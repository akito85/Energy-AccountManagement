package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_ENTITY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWEntityRepo extends PagingAndSortingRepository<VW_ENTITY, Integer>, JpaSpecificationExecutor<VW_ENTITY> {
    List<VW_ENTITY> findAll();
}

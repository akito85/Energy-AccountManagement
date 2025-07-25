package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_POS_HIER_EMP;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface VWPosHierEmpRepo extends PagingAndSortingRepository<VW_POS_HIER_EMP, Integer>, JpaSpecificationExecutor<VW_POS_HIER_EMP> {

    public List<VW_POS_HIER_EMP> findAll();
    
}
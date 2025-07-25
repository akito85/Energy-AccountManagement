package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_FILE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWFileRepo extends PagingAndSortingRepository<VW_FILE,Integer>, JpaSpecificationExecutor<VW_FILE> {
    VW_FILE findTopByFileId(Integer fileId);

    VW_FILE findTopByFileName(String fileName);
}

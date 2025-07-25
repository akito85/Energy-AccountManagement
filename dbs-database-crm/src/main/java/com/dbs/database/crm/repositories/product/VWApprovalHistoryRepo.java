package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.VW_APPROVAL_HISTORY;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface VWApprovalHistoryRepo extends PagingAndSortingRepository<VW_APPROVAL_HISTORY,String>, JpaSpecificationExecutor<VW_APPROVAL_HISTORY> {

	
   Optional<List<VW_APPROVAL_HISTORY>> findAllByIdTrans(Integer id);
   Optional<List<VW_APPROVAL_HISTORY>> findAllBytAppId(Integer id);
   Optional<List<VW_APPROVAL_HISTORY>> findAllByIdTransAndEventType(Integer id, String eventType);
  
}


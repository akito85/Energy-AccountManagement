package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.database.crm.entities.usermanagement.view.VW_HIER_FLOW;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWHierFlowRepo extends PagingAndSortingRepository<VW_HIER_FLOW, Integer>, JpaSpecificationExecutor<VW_HIER_FLOW> {
    Optional<List<VW_HIER_FLOW>> findAllByPositionIdAndIsSubmitter(Integer submitterPos, Boolean isSubmit);
    
    Optional<VW_HIER_FLOW> findByAppHierIdAndPositionIdAndIsSubmitter(Integer apphierId, Integer position, Boolean isSubmit);
    
    List<VW_HIER_FLOW> findByAppHierId(Integer apphierId);
    
    Optional<VW_HIER_FLOW> findFirstByAppHierIdAndApprovalLevel(Integer appHierId, Integer lvl);
    
    Optional<VW_HIER_FLOW> findBytAppId(Integer tAppId);
    
    Optional<VW_HIER_FLOW> findBytAppIdAndApphierDtlId(Integer tAppId, Integer apphierDtlId);
}
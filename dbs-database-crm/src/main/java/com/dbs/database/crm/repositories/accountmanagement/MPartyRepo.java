package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.database.crm.entities.accountmanagement.M_PARTY;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPartyRepo extends PagingAndSortingRepository<M_PARTY, Integer>, JpaSpecificationExecutor<M_PARTY> {
    
    Optional<M_PARTY> findByPartyId(Integer partyId);
}

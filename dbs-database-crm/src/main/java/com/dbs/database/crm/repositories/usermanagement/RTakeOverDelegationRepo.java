package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.payment.R_PAY_BANK_CONTACT;
import com.dbs.database.crm.entities.usermanagement.R_TAKE_OVER_DELEGATION;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RTakeOverDelegationRepo extends PagingAndSortingRepository<R_TAKE_OVER_DELEGATION, Integer>, JpaSpecificationExecutor<R_TAKE_OVER_DELEGATION> {
}

package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.view.VW_SUPPORT_BUCKET_UN_APP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwSupportBucketUnAppRepo extends JpaRepository<VW_SUPPORT_BUCKET_UN_APP, String> {

    Optional<VW_SUPPORT_BUCKET_UN_APP> findByAccountNumberAndBillingPeriodBetween(String accountNumber, Date startDate, Date endDate);
}

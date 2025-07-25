package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;

import com.dbs.database.crm.entities.accountmanagement.VW_TOS_SUBMISSION_DTL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
@Repository
@Transactional(value = "crmTransactionManager")
public interface VwTosSubmissionDtlRepo extends JpaRepository<VW_TOS_SUBMISSION_DTL, Integer> {

    @Query("SELECT a FROM VW_TOS_SUBMISSION_DTL a WHERE a.tosSubmissionId = :tosSubmissionId")
    List<VW_TOS_SUBMISSION_DTL> findByTosSubmissionId(Integer tosSubmissionId);
    List<VW_TOS_SUBMISSION_DTL> findAllByTosSubmissionId(Integer tosSubmissionId);
}

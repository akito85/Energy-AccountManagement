package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.M_PAY_GL_ACCOUNT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface GlAccountRepo extends PagingAndSortingRepository<M_PAY_GL_ACCOUNT, Long>, JpaSpecificationExecutor<M_PAY_GL_ACCOUNT> {

    List<M_PAY_GL_ACCOUNT> findAll();

    List<M_PAY_GL_ACCOUNT> findAllByStatusIgnoreCase(String status);
}

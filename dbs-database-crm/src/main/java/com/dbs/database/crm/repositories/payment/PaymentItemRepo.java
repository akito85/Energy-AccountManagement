package com.dbs.database.crm.repositories.payment;

import com.dbs.database.crm.entities.payment.M_PAY_PAYMENT_ITEM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface PaymentItemRepo extends JpaRepository<M_PAY_PAYMENT_ITEM, Long> {

    List<M_PAY_PAYMENT_ITEM> findAllByStatusAndStatusApproval(String status, String statusApproval);

    boolean existsByPaymentItemCodeIgnoreCaseAndNameIgnoreCase(String paymentItemCode, String name);

    boolean existsByIdNotAndPaymentItemCodeIgnoreCaseAndNameIgnoreCase(Long id, String paymentItemCode, String name);

    boolean existsByNameIgnoreCase(String name);
    boolean existsByIdNotAndNameIgnoreCase(Long id, String name);
    boolean existsByPaymentItemCodeIgnoreCase(String paymentItemCode);
    boolean existsByIdNotAndPaymentItemCodeIgnoreCase(Long id, String paymentItemCode);

}

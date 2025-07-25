package com.dbs.database.crm.repositories.product.promo;

import com.dbs.database.crm.entities.product.promo.VW_PROMO_TIERING;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface CustomCriteriaTieringRepo extends JpaRepository<VW_PROMO_TIERING, Integer>, CustomCriteriaTieringRepository {
}

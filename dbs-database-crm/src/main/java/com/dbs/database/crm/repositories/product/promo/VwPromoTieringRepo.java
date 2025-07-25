package com.dbs.database.crm.repositories.product.promo;

import com.dbs.database.crm.entities.product.promo.VW_PROMO;
import com.dbs.database.crm.entities.product.promo.VW_PROMO_TIERING;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPromoTieringRepo extends PagingAndSortingRepository<VW_PROMO_TIERING, Integer>, JpaSpecificationExecutor<VW_PROMO_TIERING> {
    List<VW_PROMO_TIERING> findAllByPricingRuleIdInOrderByTieringIdAsc(List<Integer> pricingRuleIdList);
    List<VW_PROMO_TIERING> findAllByPricingRuleId (Integer id);

    Optional<VW_PROMO_TIERING> findByTieringId(int id);

    Optional<VW_PROMO_TIERING> findByPricingRuleId(Integer id);
}

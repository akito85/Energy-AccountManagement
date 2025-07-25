package com.dbs.database.crm.repositories.rbi.view;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_POS_FINAL_PRICE_ADJ;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPosFinalPriceAdjRepo extends PagingAndSortingRepository<VW_POS_FINAL_PRICE_ADJ, Integer>, JpaSpecificationExecutor<VW_POS_FINAL_PRICE_ADJ> {
    Optional<List<VW_POS_FINAL_PRICE_ADJ>> findAllByPriceCode(String priceCode);
}

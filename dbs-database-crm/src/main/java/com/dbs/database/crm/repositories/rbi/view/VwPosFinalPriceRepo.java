package com.dbs.database.crm.repositories.rbi.view;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_POS_FINAL_PRICE;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwPosFinalPriceRepo extends PagingAndSortingRepository<VW_POS_FINAL_PRICE,Integer>, JpaSpecificationExecutor<VW_POS_FINAL_PRICE> {
    
    Optional<VW_POS_FINAL_PRICE> findByIdAndPriceCode(Integer id, String priceCode);
    
    Optional<VW_POS_FINAL_PRICE> findTopById(Integer id);
}

package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_CALCULATION_COST_CENTER;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RRbiCalcCostCenter extends PagingAndSortingRepository<R_RBI_CALCULATION_COST_CENTER,Integer>, JpaSpecificationExecutor<R_RBI_CALCULATION_COST_CENTER> {

    Optional<R_RBI_CALCULATION_COST_CENTER> findAllByCalCode (String calCode);
}

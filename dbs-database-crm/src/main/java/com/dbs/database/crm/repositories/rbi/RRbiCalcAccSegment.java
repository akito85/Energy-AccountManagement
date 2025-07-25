package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_CALCULATION_ACCOUNT_SEGMENT;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RRbiCalcAccSegment extends PagingAndSortingRepository<R_RBI_CALCULATION_ACCOUNT_SEGMENT,Integer>, JpaSpecificationExecutor<R_RBI_CALCULATION_ACCOUNT_SEGMENT> {

    Optional<R_RBI_CALCULATION_ACCOUNT_SEGMENT> findAllByCalCode (String calCode);
}

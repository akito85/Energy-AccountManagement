package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.R_RBI_CALCULATION_METER_READING_CODE;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RRbiMeterReadingCode extends PagingAndSortingRepository<R_RBI_CALCULATION_METER_READING_CODE,Integer>, JpaSpecificationExecutor<R_RBI_CALCULATION_METER_READING_CODE> {

    Optional<R_RBI_CALCULATION_METER_READING_CODE> findAllByCalCode (String calCode);
}

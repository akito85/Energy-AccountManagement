package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.TEMP_RBI_USAGE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface TempUsageRepository extends PagingAndSortingRepository<TEMP_RBI_USAGE, Integer>, JpaSpecificationExecutor<TEMP_RBI_USAGE> {

    default Specification<TEMP_RBI_USAGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<TEMP_RBI_USAGE> specification = null;
        int i = 0;
        for(String sr : pagingData.getSearch()){
            specification =
                    i == 0?
                            (Specification<TEMP_RBI_USAGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<TEMP_RBI_USAGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }
    default Specification<TEMP_RBI_USAGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<TEMP_RBI_USAGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }
    default Specification<TEMP_RBI_USAGE> addDefaultFilters(Specification<TEMP_RBI_USAGE> specification, Map<String, Object> filter, Boolean isFirst){
        specification = (Specification<TEMP_RBI_USAGE>) PagingUtils.createBatchIdFilter(specification, Integer.parseInt(filter.get("batchId").toString()), isFirst);

        return specification;
    }

    List<TEMP_RBI_USAGE> findByBatchIdAndStatus(Integer batchId, String status);

    Optional<TEMP_RBI_USAGE> findByRecordId(Integer recordId);
    Optional<TEMP_RBI_USAGE> findByRecordIdAndBatchId(Integer recordId, Integer batchId);

    @Query(value = "SELECT 1 FROM TEMP_RBI_USAGE tru WHERE tru.ACCOUNT_NUMBER = :accountNumber AND tru.ASSET_SERIAL_NUM = :assetSerialNumber AND tru.MEAS_DATE = TO_TIMESTAMP(:measDate, 'DD Mon YYYY HH24:MI:SS') AND ((:taxationRowId IS NULL AND tru.TAXATION_ROW_ID IS NULL) OR (:taxationRowId IS NOT NULL AND tru.TAXATION_ROW_ID = :taxationRowId))AND tru.SOURCES = :sources AND (STATUS = 'WAITING APPROVAL' OR STATUS = 'DRAFT' OR STATUS = 'APPROVE') AND tru.IS_DELETED = 'N'", nativeQuery = true)
    List<Object[]> usageValidation(String accountNumber, String assetSerialNumber, String measDate, String taxationRowId, String sources);

    @Procedure(name = "SP_CAL_USAGE")
    void callSp();
}


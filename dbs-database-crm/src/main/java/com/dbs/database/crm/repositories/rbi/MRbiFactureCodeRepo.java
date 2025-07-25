package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_FACTURE_CODE;
import java.util.List;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value= "crmTransactionManager")
public interface MRbiFactureCodeRepo extends PagingAndSortingRepository<M_RBI_FACTURE_CODE, Integer>, JpaSpecificationExecutor<M_RBI_FACTURE_CODE> {


    @SuppressWarnings("unchecked")
    default Specification<M_RBI_FACTURE_CODE> getSpecificationFromFilters(MaterialTablePagingRequest pagingData, Map<String, Object> filter) {
        Specification<M_RBI_FACTURE_CODE> specification = null;
        int i = 0;
        for (String sr : pagingData.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_RBI_FACTURE_CODE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_FACTURE_CODE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter, false);
        return specification;
    }

    default Specification<M_RBI_FACTURE_CODE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_RBI_FACTURE_CODE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    @SuppressWarnings("unchecked")
    default Specification<M_RBI_FACTURE_CODE> addDefaultFilters(Specification<M_RBI_FACTURE_CODE> specification,
                                                                  Map<String, Object> filter, Boolean isFirst) {
//        specification = (Specification<M_RBI_FACTURE_CODE>) PagingUtils
//                .createCommonColumnNumberEqualsFilter(specification, "billingCycleId", Long.parseLong(filter.get("billingCycleId").toString()), isFirst);
        return specification;
    }

    public List<M_RBI_FACTURE_CODE> findByStatus(String status);
    
    

}
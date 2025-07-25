package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.R_GAS_SOURCE_DETAIL;
import lombok.extern.flogger.Flogger;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RGasSourceDetailRepo extends PagingAndSortingRepository<R_GAS_SOURCE_DETAIL, Integer>, JpaSpecificationExecutor<R_GAS_SOURCE_DETAIL> {
    default Specification<R_GAS_SOURCE_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_GAS_SOURCE_DETAIL> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_GAS_SOURCE_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_GAS_SOURCE_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        specification = addDefaultFilters(specification, filter,false);
        return specification;
    }

    default Specification<R_GAS_SOURCE_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_GAS_SOURCE_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_GAS_SOURCE_DETAIL> addDefaultFilters(Specification<R_GAS_SOURCE_DETAIL> specification, Map<String, Object> filter, Boolean isFirst) {
        if(isFirst){
            if(!ObjectUtils.isEmpty(filter.get("gasSourceId"))){
                specification = (Specification<R_GAS_SOURCE_DETAIL>) where(PagingUtils.createSpecification("gasSourceId~"+filter.get("gasSourceId").toString(),EQUALS_SELECTOR));
            }
        }else{
            if(!ObjectUtils.isEmpty(filter.get("gasSourceId"))){
                specification =  specification.and((Specification<R_GAS_SOURCE_DETAIL>) PagingUtils.createSpecification("gasSourceId~"+filter.get("gasSourceId").toString(),EQUALS_SELECTOR));
            }
        }
        return specification;
    }
    R_GAS_SOURCE_DETAIL findByGasSourceIdAndDocumentNumber(Integer gasSourceId, String documentNumber);
    Optional<R_GAS_SOURCE_DETAIL> findByGasSourceDetailId(Integer gasSourceDetailId);
    List<R_GAS_SOURCE_DETAIL> findAll();
    List<R_GAS_SOURCE_DETAIL> findByGasSourceIdAndStatus(Integer gasSourceId, String status);
    List<R_GAS_SOURCE_DETAIL> findAllByGasSourceIdAndStatus(Integer gasSourceId, String status);
    List<R_GAS_SOURCE_DETAIL> findAllByGasSourceIdAndStatusAndGasSourceDetailIdNot(Integer gasSourceId, String status, Integer gasSourceDetailId);
    List<R_GAS_SOURCE_DETAIL> findAllByGasSourceId(Integer gasSourceId);

    Optional<R_GAS_SOURCE_DETAIL> findTopByGasSourceIdAndStatusOrderByStartDateDesc(Integer gasSourceId, String status);

    List<R_GAS_SOURCE_DETAIL> findAllByGasSourceIdOrderByCreatedDateAsc(Integer gasSourceId);

    List<R_GAS_SOURCE_DETAIL> findAllByGasSourceIdAndStatusOrderByCreatedDateDesc(Integer gasSourceId, String status);
}

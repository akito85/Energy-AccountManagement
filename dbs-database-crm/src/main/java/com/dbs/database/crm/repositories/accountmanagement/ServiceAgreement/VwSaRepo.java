package com.dbs.database.crm.repositories.accountmanagement.ServiceAgreement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_SA;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import io.micrometer.observation.annotation.Observed;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwSaRepo extends PagingAndSortingRepository<VW_SA, Integer>, JpaSpecificationExecutor<VW_SA> {

    default Specification<VW_SA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_SA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_SA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_SA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<VW_SA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_SA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_SA> addDefaultFilters(Specification<VW_SA> specification, Map<String, Object> filter, Boolean isFirst) {

        if(filter.get("accountId") != null) {
            specification = specification == null ? (Specification<VW_SA>) PagingUtils.createSpecification("accountId~" + (filter.get("accountId")),DEFAULT_SELECTOR) : specification.and((Specification<VW_SA>) PagingUtils.createSpecification("accountId~" + (filter.get("accountId")),DEFAULT_SELECTOR));
        }

        return specification;
    }
    Optional<VW_SA> findByAccountId(Integer accountId);
    List<VW_SA> findAllByAccountId(Integer accountId);
    Optional<VW_SA> findTopByAccountIdAndIsMainAndApprovalStatusIn(Integer accountId, String isMain, List<String> status);

    Optional<VW_SA> findTopByAccountIdAndIsMainAndApprovalStatusInAndIdNot(Integer accountId, String isMain, List<String> status, Integer id);

    List<VW_SA> findAllByIsMainAndStatus(String isMain, String status);

    Optional<VW_SA> findBySaNumberAndIsMainAndStatus (String saNumb, String isMain, String status);

    VW_SA findByAccountNumberAndSaNumberAndIsMain (String accNumb, String saNumb, String isMain);
    Optional<List<VW_SA>> findAllByAccountIdAndServiceTypeAndStatusAndIsMain(Integer accountId, String serviceType, String status, String isMain);

    List<VW_SA> findAllByAccountIdAndSaTypeAndStatus(Integer accountId, String saType, String status);

    List<VW_SA> findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInOrderByIdAsc(Integer accountId, String saReferenceNumber, String saType, List<String> status);

    List<VW_SA> findAllByAccountIdAndSaReferenceNumberAndSaTypeAndApprovalStatusInAndIdNotOrderByIdAsc(Integer accountId, String saReferenceNumber, String saType, List<String> status, Integer id);
    
    Optional<VW_SA> findBySaNumberAndStatus (String saNumb, String status);

}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_APPROVAL_HIERARCHY;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
public interface MApprovalHierarchyRepo extends PagingAndSortingRepository<M_APPROVAL_HIERARCHY, Integer>, JpaSpecificationExecutor<M_APPROVAL_HIERARCHY> {

    Optional<List<M_APPROVAL_HIERARCHY>> findAllByAppHierIdInAndApprovalType(List<Integer> idList, String approvalType);

    Optional<M_APPROVAL_HIERARCHY> findByAppHierId(Integer appHierId);

    @Query("SELECT u FROM M_APPROVAL_HIERARCHY u WHERE LOWER(u.approvalName) = LOWER(:approvalName)")
    Optional<M_APPROVAL_HIERARCHY> findByApprovalName(String approvalName);

    List<M_APPROVAL_HIERARCHY> findAllByApprovalName(String approvalName);

    default Specification<M_APPROVAL_HIERARCHY> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_APPROVAL_HIERARCHY> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_APPROVAL_HIERARCHY>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_APPROVAL_HIERARCHY>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_APPROVAL_HIERARCHY> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_APPROVAL_HIERARCHY> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_APPROVAL_HIERARCHY> addDefaultFilters(Specification<M_APPROVAL_HIERARCHY> specification, Map<String, Object> filter, Boolean isFirst){
        if (filter.get("entityId") != null) {
            specification = (Specification<M_APPROVAL_HIERARCHY>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        }
        return specification;
    }
    Optional<List<M_APPROVAL_HIERARCHY>> findByStatusAndIsDeleted(String status, Boolean isDeleted);

    List<M_APPROVAL_HIERARCHY> findAll();
    
    @Query(value = "SELECT maph.* FROM M_APPROVAL_HIERARCHY maph INNER JOIN M_APPROVAL_HIERARCHY_DTL maphd ON maph.APPHIER_ID = maphd.APPHIER_ID WHERE maph.STATUS = 'ACTIVE' AND maphd.POSITION_ID = :positionId AND maphd.IS_SUBMITTER = :isSubmitter AND lower(APPROVAL_TYPE) = :approvalType", nativeQuery = true)
    List<M_APPROVAL_HIERARCHY> findApprovalHierarchyByPosition(Integer positionId, String isSubmitter, String approvalType);

    @Query(value = "SELECT maph.* FROM M_APPROVAL_HIERARCHY maph INNER JOIN M_APPROVAL_HIERARCHY_DTL maphd ON maph.APPHIER_ID = maphd.APPHIER_ID WHERE maph.STATUS = 'ACTIVE' AND maphd.POSITION_ID = :positionId AND maphd.IS_SUBMITTER = :isSubmitter AND APPROVAL_TYPE = :approvalType", nativeQuery = true)
    List<M_APPROVAL_HIERARCHY> findApprovalHierarchyByPositionAndType(Integer positionId, String isSubmitter, String approvalType);
}

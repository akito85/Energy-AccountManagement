package com.dbs.database.crm.repositories.usermanagement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.M_ATTACHMENT;
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
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAttachmentRepo extends PagingAndSortingRepository<M_ATTACHMENT, Integer>, JpaSpecificationExecutor<M_ATTACHMENT> {
    default Specification<M_ATTACHMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ATTACHMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ATTACHMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ATTACHMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_ATTACHMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ATTACHMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ATTACHMENT> addDefaultFilters(Specification<M_ATTACHMENT> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if (!ObjectUtils.isEmpty(filter.get("referenceIdIn"))) {
            List<Integer> refIds = (List<Integer>) filter.get("referenceIdIn");
            specification = (Specification<M_ATTACHMENT>) PagingUtils.createRefIdInFilter(specification, refIds, true);
        } else
            specification = (Specification<M_ATTACHMENT>) PagingUtils.createSpecification("referenceId~" + (filter.get("referenceId")),DEFAULT_SELECTOR);

        specification = specification.and((Specification<M_ATTACHMENT>)PagingUtils.createSpecification("category~"+filter.get("category"),DEFAULT_SELECTOR));


        return specification;
    }
    @Query("SELECT a FROM M_ATTACHMENT a WHERE a.referenceId = :referenceId AND a.id NOT IN :id")
    List<M_ATTACHMENT> findNotIn(Integer referenceId, List<Integer> id);

    List<M_ATTACHMENT> findByReferenceIdAndIdIn(Integer referenceId, List<Integer> id);

    Optional<List<M_ATTACHMENT>> findByReferenceId(Integer referenceId);

    Optional<List<M_ATTACHMENT>> findByReferenceIdAndCategoryIgnoreCase(Integer referenceId, String cat);

    Optional<List<M_ATTACHMENT>> findByReferenceIdAndIsDraftAndIsDeletedAndCategoryIgnoreCase(Integer referenceId,boolean isDraft, boolean isDeleted, String cat);

    Optional<List<M_ATTACHMENT>> findByReferenceIdAndIsDeletedAndCategoryIgnoreCase(Integer referenceId, boolean isDeleted, String cat);

    Optional<M_ATTACHMENT> findByReferenceIdAndIsDraftAndIsDeletedAndCategory(Integer referenceId,boolean isDraft, boolean isDeleted, String cat);

    List<M_ATTACHMENT> findAllByReferenceId(Integer referenceId);

    List<M_ATTACHMENT> findAllByReferenceIdAndIsDeletedAndCategoryIgnoreCase(Integer referenceId, boolean isDeleted, String cat);
}

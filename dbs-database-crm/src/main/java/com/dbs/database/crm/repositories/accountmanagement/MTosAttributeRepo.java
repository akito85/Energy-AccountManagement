package com.dbs.database.crm.repositories.accountmanagement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.accountmanagement.R_TOS_ATTRIBUTE;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MTosAttributeRepo extends PagingAndSortingRepository<R_TOS_ATTRIBUTE, Integer>, JpaSpecificationExecutor<R_TOS_ATTRIBUTE> {

    default Specification<R_TOS_ATTRIBUTE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<R_TOS_ATTRIBUTE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<R_TOS_ATTRIBUTE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<R_TOS_ATTRIBUTE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<R_TOS_ATTRIBUTE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<R_TOS_ATTRIBUTE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<R_TOS_ATTRIBUTE> addDefaultFilters(Specification<R_TOS_ATTRIBUTE> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        specification = (Specification<R_TOS_ATTRIBUTE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        specification = specification.and((Specification<R_TOS_ATTRIBUTE>)PagingUtils.createSpecification("mTosId~"+filter.get("mTosId"),DEFAULT_SELECTOR));

        /*INFO: Add Cost Center Filter*/
        return specification;
    }
    
    @Query("SELECT a FROM R_TOS_ATTRIBUTE a WHERE a.idTos = :idTos AND a.attributeId NOT IN :attributeIds")
    List<R_TOS_ATTRIBUTE> findNotIn(Integer idTos, List<Integer> attributeIds);
    
    Optional<List<R_TOS_ATTRIBUTE>> findByIdTos(Integer idTos);

}

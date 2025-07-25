package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_TAXIMPLICATION;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MTaxImplicationRepo extends PagingAndSortingRepository<M_AM_TAXIMPLICATION, Integer>, JpaSpecificationExecutor<M_AM_TAXIMPLICATION> {
    
    default Specification<M_AM_TAXIMPLICATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_TAXIMPLICATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_TAXIMPLICATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_TAXIMPLICATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_AM_TAXIMPLICATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_TAXIMPLICATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_TAXIMPLICATION> addDefaultFilters(Specification<M_AM_TAXIMPLICATION> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<M_AM_TAXIMPLICATION>) PagingUtils.createSpecification("id~" + filter.get("id"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<M_AM_TAXIMPLICATION>) PagingUtils.createSpecification("id~" + filter.get("id"), EQUALS_SELECTOR));
        }
        return specification;
    }

    @Query(value = "SELECT DISTINCT mat.* FROM M_AM_TAXIMPLICATION mat INNER JOIN M_AM_TAXIMPLICATION_RULE matr ON MAT.ID = MATR.M_AM_TAXIMPLICATION_ID WHERE mat.CATEGORY = :category AND mat.SERVICE_TYPE = :serviceType AND mat.STATUS = 'ACTIVE' AND matr.STATUS = 'ACTIVE'", nativeQuery = true)
    List<M_AM_TAXIMPLICATION> findAllByCategoryAndServiceType(Integer category, Integer serviceType);
    
    List<M_AM_TAXIMPLICATION> findAll();

    Boolean existsByTaxImplicationName(String taxImplicationName);

    Optional<M_AM_TAXIMPLICATION> findTopByTaxImplicationNameIgnoreCaseAndCategoryAndServiceType(String taxImplicationName, Integer category, Integer serviceType);
}

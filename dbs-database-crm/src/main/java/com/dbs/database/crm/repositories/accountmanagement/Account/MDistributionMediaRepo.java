package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_DISTRIBUTION_MEDIA;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MDistributionMediaRepo extends PagingAndSortingRepository<M_DISTRIBUTION_MEDIA, Integer>, JpaSpecificationExecutor<M_DISTRIBUTION_MEDIA> {
    
    default Specification<M_DISTRIBUTION_MEDIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_DISTRIBUTION_MEDIA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_DISTRIBUTION_MEDIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_DISTRIBUTION_MEDIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_DISTRIBUTION_MEDIA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_DISTRIBUTION_MEDIA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_DISTRIBUTION_MEDIA> addDefaultFilters(Specification<M_DISTRIBUTION_MEDIA> specification, Map<String, Object> filter, Boolean isFirst){
        
        if(filter.get("accountId") != null) {
            if(specification == null) {
    		specification =(Specification<M_DISTRIBUTION_MEDIA>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
            } else {
                specification =specification.and((Specification<M_DISTRIBUTION_MEDIA>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
            }
        }

        return specification;
    } 
    
    Page<M_DISTRIBUTION_MEDIA> findAll(Specification<M_DISTRIBUTION_MEDIA> specification, Pageable paging);
    
    List<M_DISTRIBUTION_MEDIA> findAll();
    
    Optional<M_DISTRIBUTION_MEDIA> findById(Integer distributionMediaId);
    
    List<M_DISTRIBUTION_MEDIA> findAllByAccountId(Integer accountId);
    
    Optional<M_DISTRIBUTION_MEDIA> findTopByAccountIdAndProductIdAndStatus(Integer accountId, Integer productId, String status);
}

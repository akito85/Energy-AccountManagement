package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.VW_ASSET_ASSIGNMENT;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAssetAssignmentRepo extends PagingAndSortingRepository<VW_ASSET_ASSIGNMENT, Integer>, JpaSpecificationExecutor<VW_ASSET_ASSIGNMENT>{
    
    default Specification<VW_ASSET_ASSIGNMENT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ASSET_ASSIGNMENT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ASSET_ASSIGNMENT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ASSET_ASSIGNMENT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_ASSET_ASSIGNMENT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ASSET_ASSIGNMENT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ASSET_ASSIGNMENT> addDefaultFilters(Specification<VW_ASSET_ASSIGNMENT> specification, Map<String, Object> filter, Boolean isFirst){
        if(specification == null) {
    		specification =(Specification<VW_ASSET_ASSIGNMENT>) PagingUtils.createSpecification("servicePointId~" + filter.get("servicePointId"), EQUALS_SELECTOR);
        } else {
    		specification =specification.and((Specification<VW_ASSET_ASSIGNMENT>) PagingUtils.createSpecification("servicePointId~" + filter.get("servicePointId"), EQUALS_SELECTOR));
        }
        return specification;
    }
    
    Optional<List<VW_ASSET_ASSIGNMENT>> findAllByServicePointId(Integer servicePointId);
    
    Optional<VW_ASSET_ASSIGNMENT> findTopByServicePointIdAndStatusOrderByIdDesc(Integer servicePointId, String status);
    
    Optional<VW_ASSET_ASSIGNMENT> findTopByAccountAddressIdAndStatus(Integer accountAddressId, String status);

    Optional<VW_ASSET_ASSIGNMENT> findTopByServicePointIdAndStatusOrderByCreatedDateDesc(Integer servicePointId, String status);

    List<VW_ASSET_ASSIGNMENT> findAllByAssetId(Integer assetId);

    List<VW_ASSET_ASSIGNMENT> findAllByAssetIdOrderByCreatedDateDesc(Integer assetId);
    List<VW_ASSET_ASSIGNMENT> findByAssetIdAndStatus(Integer assetId, String status);

    Optional<VW_ASSET_ASSIGNMENT> findTopByAssetIdAndStatus(Integer assetId, String status);
}

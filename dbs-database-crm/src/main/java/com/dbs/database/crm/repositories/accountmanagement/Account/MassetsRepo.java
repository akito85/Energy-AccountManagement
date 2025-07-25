package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ASSETS;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MassetsRepo extends PagingAndSortingRepository<M_ASSETS, Integer>, JpaSpecificationExecutor<M_ASSETS> {
    boolean existsByAssetName(Integer assetNameId);

    default Specification<M_ASSETS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata) {
        Specification<M_ASSETS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ASSETS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ASSETS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_ASSETS> getSpecificationDefault() {
        Specification<M_ASSETS> specification = null;
        specification = addDefaultFilters(specification,true);
        return specification;
    }

    default Specification<M_ASSETS> addDefaultFilters(Specification<M_ASSETS> specification, Boolean isFirst){
        return specification;
    }

    //master
    default Specification<M_ASSETS> getSpecificationFromFiltersMaster(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ASSETS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ASSETS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ASSETS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFiltersMaster(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_ASSETS> getSpecificationDefaultMaster(Map<String, Object> filter) {
        Specification<M_ASSETS> specification = null;
        specification = addDefaultFiltersMaster(specification, filter, true);
        return specification;
    }

    default Specification<M_ASSETS> addDefaultFiltersMaster(Specification<M_ASSETS> specification, Map<String, Object> filter, Boolean isFirst){
        return specification;
    }
    
    @Query("SELECT a FROM M_ASSETS a WHERE a.status = :status1 OR a.status = :status2 ORDER BY a.createdDate DESC")
    List<M_ASSETS> findAllByStatus(String status1, String status2);
    
    List<M_ASSETS> findAllByAssetName(Integer assetName);
    
    @Query("SELECT a FROM M_ASSETS a WHERE a.assetName = :assetName AND (a.status = :status1 OR a.status = :status2)")
    Optional<M_ASSETS> findByAssetNameAndStatus(Integer assetName, String status, String status2);
    
    Optional<M_ASSETS> findById(Integer id);
    
    Boolean existsBySerialNumber(String serialNumber);

    Optional<M_ASSETS> findBySerialNumberIgnoreCaseAndBrand(String serialNumber, Integer brand);
}

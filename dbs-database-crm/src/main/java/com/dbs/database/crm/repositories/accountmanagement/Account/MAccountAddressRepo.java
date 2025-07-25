package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.dbs.database.crm.entities.accountmanagement.M_ACCOUNT_ADDRESS;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAccountAddressRepo extends PagingAndSortingRepository<M_ACCOUNT_ADDRESS, Integer>, JpaSpecificationExecutor<M_ACCOUNT_ADDRESS> {

    default Specification<M_ACCOUNT_ADDRESS> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ACCOUNT_ADDRESS> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ACCOUNT_ADDRESS>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ACCOUNT_ADDRESS>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_ACCOUNT_ADDRESS> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ACCOUNT_ADDRESS> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ACCOUNT_ADDRESS> addDefaultFilters(Specification<M_ACCOUNT_ADDRESS> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("accountId") != null) {
            specification = (Specification<M_ACCOUNT_ADDRESS>) PagingUtils.createSpecification("accountId~" + (filter.get("accountId")),DEFAULT_SELECTOR);
        }
        return specification;
    } 
    
    List<M_ACCOUNT_ADDRESS> findAllByAccountIdAndStatusIgnoreCase(Integer accountId, String status);

    @Query("SELECT a FROM M_ACCOUNT_ADDRESS a WHERE a.accountId = :accountId AND a.status = :status AND a.premiseFlag = :premise")
    Page<M_ACCOUNT_ADDRESS> findAllByAccountIdAndStatusAndPremiseFlagIsTrue(Pageable pageable, Integer accountId, String status, Boolean premise);
    
    @Query("SELECT a FROM M_ACCOUNT_ADDRESS a WHERE a.accountId = :accountId AND a.premiseFlag = :premise")
    Page<M_ACCOUNT_ADDRESS> findAllByAccountIdAndPremiseFlagIsTrue(Pageable pageable, Integer accountId, Boolean premise);
    
    Optional<M_ACCOUNT_ADDRESS> findTopByAccountIdAndBusinessPurpose(Integer accountId, Integer businessPurpose);

    List<M_ACCOUNT_ADDRESS> findAllByAccountIdAndPrimaryFlagAndStatus(Integer accountId, Boolean primaryFlag, String status);

    List<M_ACCOUNT_ADDRESS> findAllByAccountIdAndBusinessPurposeAndStatus(Integer accountId, Integer businessPurpose, String status);
    
    Optional<M_ACCOUNT_ADDRESS> findTopByAddressIdAndStatusAndPremiseFlagIsTrue(Integer addressId, String status);
    
    Optional<M_ACCOUNT_ADDRESS> findById(Integer id);

    List<M_ACCOUNT_ADDRESS> findAllByAccountId(Integer accountId);
    
    Optional<M_ACCOUNT_ADDRESS> findTopByAddressIdAndPremiseFlagAndStatus(Integer addressId, Boolean premiseFlag, String status);
    
    Optional<M_ACCOUNT_ADDRESS> findTopByAccountIdAndStatusAndPrimaryFlagIsTrue(Integer accountId, String status);

    Optional<M_ACCOUNT_ADDRESS> findTopByAccountIdAndAddressIdAndBusinessPurposeAndStatus(Integer accountId, Integer addressId, Integer businessPurpose, String status);

    Optional<M_ACCOUNT_ADDRESS> findTopByAddressIdAndStatus(Integer addressId, String status);

}

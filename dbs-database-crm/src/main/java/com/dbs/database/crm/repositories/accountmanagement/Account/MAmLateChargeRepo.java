package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_AM_LATECHARGE;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MAmLateChargeRepo extends PagingAndSortingRepository<M_AM_LATECHARGE, Integer>, JpaSpecificationExecutor<M_AM_LATECHARGE> {

    default Specification<M_AM_LATECHARGE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_AM_LATECHARGE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_AM_LATECHARGE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_AM_LATECHARGE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_AM_LATECHARGE> addDefaultFilters(Specification<M_AM_LATECHARGE> specification, Map<String, Object> filter, Boolean isFirst){
//        if(specification == null) {
//    		specification =(Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR);
//        } else {
//    		specification =specification.and((Specification<M_AM_LATECHARGE>) PagingUtils.createSpecification("accountId~" + filter.get("accountId"), EQUALS_SELECTOR));
//        }
        return specification;
    } 
    
    List<M_AM_LATECHARGE> findAll();
    
    
    @Query(value = "SELECT * FROM M_AM_LATECHARGE WHERE ID =:id AND status = 'ACTIVE'", nativeQuery = true)
    Optional<M_AM_LATECHARGE> findByIdAndStatus(Integer id);

    @Query(value = "SELECT DISTINCT mal.* FROM M_AM_LATECHARGE mal INNER JOIN M_AM_LATECHARGE_RULE malr ON mal.ID = malr.M_AM_LATECHARGE_ID WHERE mal.CURRENCY = :currency AND mal.STATUS = 'ACTIVE' AND malr.STATUS = 'ACTIVE'", nativeQuery = true)
    List<M_AM_LATECHARGE> findAllByCurrency(Integer currency);

    Optional<M_AM_LATECHARGE> findTopByLateChargeNameIgnoreCaseAndCurrency(String lateChargeName, Integer currency);
}

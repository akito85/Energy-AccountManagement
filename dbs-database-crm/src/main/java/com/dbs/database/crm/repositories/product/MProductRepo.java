package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductRepo extends PagingAndSortingRepository<M_PRODUCT,String>, JpaSpecificationExecutor<M_PRODUCT> {

    default Specification<M_PRODUCT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PRODUCT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PRODUCT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PRODUCT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_PRODUCT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PRODUCT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PRODUCT> addDefaultFilters(Specification<M_PRODUCT> specification, Map<String, Object> filter, Boolean isFirst){

        specification = (Specification<M_PRODUCT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
        specification = (Specification<M_PRODUCT>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        
        if(filter.get("status")!=null)
        	specification = specification.and((Specification<M_PRODUCT>) PagingUtils.createSpecification("status~"+filter.get("status"), EQUALS_SELECTOR));
        if(filter.get("serviceType")!= null)
            specification = specification.and((Specification<M_PRODUCT>) PagingUtils.createSpecification("serviceType~"+filter.get("serviceType"), EQUALS_SELECTOR));
        if(filter.get("productType")!=null)
            specification = specification.and((Specification<M_PRODUCT>) PagingUtils.createSpecification("productType~"+filter.get("productType"), EQUALS_SELECTOR));
        return specification;
    }

    Optional<M_PRODUCT> findTopById(Integer id);
    
    Optional <M_PRODUCT> findById(Integer id);
    
    Optional<List<M_PRODUCT>> findAllByStatus(String status);
    M_PRODUCT findTopByProductNameIgnoreCase(String productName);
    
    List<M_PRODUCT> findAll();

    List<M_PRODUCT> findAllByServiceTypeAndProductType(Integer serviceType, Integer productType);

    @Query(value = "SELECT a FROM M_PRODUCT a WHERE a.serviceType NOT IN :serviceType")
    Page<M_PRODUCT> findAllOrderByNotInTable(Integer serviceType, Pageable pageable);

    @Query("SELECT DISTINCT a.id FROM M_PRODUCT a WHERE a.status ='ACTIVE' AND (a.endDate > CURRENT_DATE OR a.endDate is null) AND a.startDate <= CURRENT_DATE")
    Optional<List<Integer>> findByEndDate();

}

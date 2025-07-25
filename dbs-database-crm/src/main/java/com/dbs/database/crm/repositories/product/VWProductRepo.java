package com.dbs.database.crm.repositories.product;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_CRITERIA;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_RULE;
import com.dbs.database.crm.entities.product.VW_PRODUCT;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.List;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWProductRepo extends PagingAndSortingRepository<VW_PRODUCT, Integer>, JpaSpecificationExecutor<VW_PRODUCT> {
    default Specification<VW_PRODUCT> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_PRODUCT> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_PRODUCT>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_PRODUCT>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_PRODUCT> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_PRODUCT> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_PRODUCT> addDefaultFilters(Specification<VW_PRODUCT> specification, Map<String, Object> filter, Boolean isFirst){

        specification = (Specification<VW_PRODUCT>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
        if(filter.get("positionId") != null) {
            CostCenterUtils costCenterUtils = new CostCenterUtils();
            List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);
            specification = (Specification<VW_PRODUCT>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        }

        if(filter.get("status") !=null){
            specification = specification.and((Specification<VW_PRODUCT>) PagingUtils.createSpecification("status~"+filter.get("status"), DEFAULT_SELECTOR));
        }
        if(filter.get("serviceType") !=null){
            specification = specification.and((Specification<VW_PRODUCT>) PagingUtils.createSpecification("serviceType~"+filter.get("serviceType"), DEFAULT_SELECTOR));
        }
        if(filter.get("productType") != null) {
            specification = specification.and((Specification<VW_PRODUCT>) PagingUtils.createSpecification("productType~"+filter.get("productType"), DEFAULT_SELECTOR));
        }
        return specification;
    }
    
    Optional<List<VW_PRODUCT>> findAllByStatusAndEntityId(String status, Integer entity);

    Optional<List<VW_PRODUCT>> findAllByStatus(String status);
    Optional<List<VW_PRODUCT>> findAllByStatusAndServiceTypeAndEntityId(String status, String serviceType, Integer entity);

    Optional<List<VW_PRODUCT>> findAllByStatusAndServiceType(String status, String serviceType);
    
    Optional<List<VW_PRODUCT>> findAllByStatusAndServiceTypeAndProductType(String status, String serviceType, String productType);

    List<VW_PRODUCT> findAllByServiceTypeNameAndProductTypeNameAndStatus(String serviceTypeName, String productTypeName, String status);

    List<VW_PRODUCT> findAllByEntityIdAndCcIdInAndServiceTypeNameAndProductTypeNameAndStatus(Integer entityId, List<Integer> ccId, String serviceTypeName, String productTypeName, String status);

    List<VW_PRODUCT> findAllByEntityIdAndServiceTypeNameAndProductTypeNameAndStatus(Integer entityId, String serviceTypeName, String productTypeName, String status);
    List<VW_PRODUCT> findAllByServiceTypeAndEntityId(String serviceType, Integer entity);

    List<VW_PRODUCT> findAllByEntityIdAndCcIdIn(Integer entityId, List<Integer> ccId);

    List<VW_PRODUCT> findAllByServiceTypeAndStatus(Integer serviceType, String status);
    Optional<VW_PRODUCT> findById(Integer id);
    
    Optional<VW_PRODUCT> findTopByIdAndStatusOrderByCreatedDateDesc(Integer id, String status);
    List<VW_PRODUCT> findAllByStatusAndEntityIdAndCcIdInAndIdNot(String status, Integer entity, List<Integer> ccId, Integer id);
    @Query(value="SELECT * FROM VW_PRODUCT WHERE SERVICE_TYPE_NAME = :serviceTypeName AND PRODUCT_TYPE_NAME = :productTypeName AND STATUS = :status AND LOWER(PRODUCT_NAME) = LOWER(:productName)", nativeQuery = true)
    Optional<VW_PRODUCT> findByServiceTypeNameAndProductTypeNameAndStatusAndProductNameIgnoreCase(String serviceTypeName, String productTypeName, String status, String productName);
    
}

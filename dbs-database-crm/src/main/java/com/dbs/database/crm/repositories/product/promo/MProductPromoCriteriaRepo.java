package com.dbs.database.crm.repositories.product.promo;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.utils.CostCenterUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import com.dbs.database.crm.entities.product.promo.M_PROMO_CRITERIA;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductPromoCriteriaRepo extends PagingAndSortingRepository<M_PROMO_CRITERIA, Integer>, JpaSpecificationExecutor<M_PROMO_CRITERIA> {

    default Specification<M_PROMO_CRITERIA> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PROMO_CRITERIA> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PROMO_CRITERIA>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PROMO_CRITERIA>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<M_PROMO_CRITERIA> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_PROMO_CRITERIA> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_PROMO_CRITERIA> addDefaultFilters(Specification<M_PROMO_CRITERIA> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        specification = (Specification<M_PROMO_CRITERIA>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);

        /*INFO: Add Cost Center Filter*/
        specification = (Specification<M_PROMO_CRITERIA>) PagingUtils.createCostCenterFilter(specification, ccList, false);


        return specification;
    }

    M_PROMO_CRITERIA findTopById(Integer id);
    
    Optional<List<M_PROMO_CRITERIA>> findAllByStatus(String status);
    
    Optional<M_PROMO_CRITERIA> findByName(String name);
    
    @Query("SELECT a FROM M_PROMO_CRITERIA a WHERE a.idPromo = :idPromo AND a.idCriteria NOT IN :idCriteria")
    List<M_PROMO_CRITERIA> findNotIn(Integer idPromo, List<Integer> idCriteria);
    
    Optional<List<M_PROMO_CRITERIA>> findByIdPromo(Integer idPromo);
}

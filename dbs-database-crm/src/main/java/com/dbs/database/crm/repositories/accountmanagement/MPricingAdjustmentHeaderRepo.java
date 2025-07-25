package com.dbs.database.crm.repositories.accountmanagement;


import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_PRICING_ADJUSTMENT_HEADER;
import com.dbs.database.crm.utils.CostCenterUtils;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MPricingAdjustmentHeaderRepo extends PagingAndSortingRepository<M_PRICING_ADJUSTMENT_HEADER,String>, JpaSpecificationExecutor<M_PRICING_ADJUSTMENT_HEADER> {

    default Specification<M_PRICING_ADJUSTMENT_HEADER> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_PRICING_ADJUSTMENT_HEADER> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_PRICING_ADJUSTMENT_HEADER>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_PRICING_ADJUSTMENT_HEADER>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_PRICING_ADJUSTMENT_HEADER> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    default Specification<M_PRICING_ADJUSTMENT_HEADER> addDefaultFilters(Specification<M_PRICING_ADJUSTMENT_HEADER> specification, Map<String, Object> filter, Boolean isFirst){

        /*INFO: Add Entity Filter*/
        specification = (Specification<M_PRICING_ADJUSTMENT_HEADER>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        CostCenterUtils costCenterUtils = new CostCenterUtils();
        List<Integer> ccList = costCenterUtils.findCostCenterByPositionId(Integer.parseInt(filter.get("positionId").toString()),GET_CC_PARENT);

        /*INFO: Add Cost Center Filter*/
        specification = (Specification<M_PRICING_ADJUSTMENT_HEADER>) PagingUtils.createCostCenterFilter(specification, ccList, false);
        
        return specification;
    }

    @Query(nativeQuery = true,value="select count(mp.id) from m_pricing mp inner join r_pricing_detail rpd on mp.id = rpd.ID_PRICING_NUMBER where rpd.id = ?1 \n" +
            "and mp.STATUS = 'ACTIVE' and mp.STATUS_APPROVAL not in('WAITING FOR APPROVAL')")
    Integer findCountActivePricing(Integer idPricingDetail);
    M_PRICING_ADJUSTMENT_HEADER findTopByIdAndEntityId(Integer id , Integer entity);
    Optional<M_PRICING_ADJUSTMENT_HEADER> findById(Integer id);
    List<M_PRICING_ADJUSTMENT_HEADER> findAll();

    M_PRICING_ADJUSTMENT_HEADER findTopByIdAndStatus (Integer id, String status);

    List<M_PRICING_ADJUSTMENT_HEADER> findAllByEntityId(Integer entity);

	List<M_PRICING_ADJUSTMENT_HEADER> findAllById(Integer id);
    List<M_PRICING_ADJUSTMENT_HEADER> findAllBymPricingDetailId(Integer id);

    Optional<M_PRICING_ADJUSTMENT_HEADER> findTopBymPricingDetailIdAndStatus(Integer mpricingDetailId, String Status);

    Optional<M_PRICING_ADJUSTMENT_HEADER> findTopByNameIgnoreCase(String name);
}



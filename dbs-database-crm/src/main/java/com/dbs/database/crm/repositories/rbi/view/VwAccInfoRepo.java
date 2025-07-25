package com.dbs.database.crm.repositories.rbi.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VwAccInfoRepo extends PagingAndSortingRepository<VW_ACCOUNT_INFORMATION,Integer>, JpaSpecificationExecutor<VW_ACCOUNT_INFORMATION> {
    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_ACCOUNT_INFORMATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_ACCOUNT_INFORMATION> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_ACCOUNT_INFORMATION> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_ACCOUNT_INFORMATION> addDefaultFilters(Specification<VW_ACCOUNT_INFORMATION> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<VW_ACCOUNT_INFORMATION>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);

        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    @Override
    public List<VW_ACCOUNT_INFORMATION> findAll();
    List<VW_ACCOUNT_INFORMATION> findByCostCenterIdIn(List<Integer> ccList);
    
    public Optional<VW_ACCOUNT_INFORMATION> findByAccountNumber(String accountNumber);

    Optional<VW_ACCOUNT_INFORMATION> findByAccountId (Integer accId);
    List<VW_ACCOUNT_INFORMATION> findAllByCustomerId(Integer customerId);
}

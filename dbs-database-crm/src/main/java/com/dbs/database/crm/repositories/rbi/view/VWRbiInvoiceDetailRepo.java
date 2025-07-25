package com.dbs.database.crm.repositories.rbi.view;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_RBI_INVOICE_DETAIL;

//@Repository
//@Transactional(value = "crmTransactionManager")
public interface VWRbiInvoiceDetailRepo extends PagingAndSortingRepository<VW_RBI_INVOICE_DETAIL, Integer>, JpaSpecificationExecutor<VW_RBI_INVOICE_DETAIL> {
    
    default Specification<VW_RBI_INVOICE_DETAIL> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_DETAIL> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_RBI_INVOICE_DETAIL>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_RBI_INVOICE_DETAIL>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
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

    default Specification<VW_RBI_INVOICE_DETAIL> getSpecificationDefault(Map<String, Object> filter) {
        Specification<VW_RBI_INVOICE_DETAIL> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<VW_RBI_INVOICE_DETAIL> addDefaultFilters(Specification<VW_RBI_INVOICE_DETAIL> specification, Map<String, Object> filter, Boolean isFirst){
//        /*INFO: Add Entity Filter*/
        return specification;
    }
    
    List<VW_RBI_INVOICE_DETAIL> findAll();
    
    Optional<VW_RBI_INVOICE_DETAIL> findFirstByInvoiceNumber(String invNumber);
}

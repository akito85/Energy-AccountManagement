package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_INVOICE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MRbiInvoiceRepo extends PagingAndSortingRepository<M_RBI_INVOICE, String>, JpaSpecificationExecutor<M_RBI_INVOICE>,
        JpaRepository<M_RBI_INVOICE, String>{
    default Specification<M_RBI_INVOICE> getSpecficationFromFilters(MaterialTablePagingRequest pagingData){
        Specification<M_RBI_INVOICE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingData.getSearch()){
            specification =
                    i == 0 ? (Specification<M_RBI_INVOICE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_RBI_INVOICE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    List<M_RBI_INVOICE> findAll();
    List<M_RBI_INVOICE> findByCcIdIn(List<Integer> ccList);
    Optional<M_RBI_INVOICE> findByInvoiceNumber(String invNumber);
    Optional<M_RBI_INVOICE> findByInvoiceNumberAndEntityId(String invNumber, Integer entityId);
    Optional<List<M_RBI_INVOICE>> findByPrefix(String prefix);

    @Query(value = "SELECT INVOICE_NUMBER FROM M_RBI_INVOICE WHERE BILLING_CODE =:billingCode", nativeQuery = true)
    String findInvoiceNumberByBillingCode(String billingCode);
    
    @Query(value = "SELECT COALESCE(TO_NUMBER(MAX(SUBSTR(INVOICE_NUMBER, 8, 15))),0) FROM M_RBI_INVOICE WHERE PREFIX = :prefix", nativeQuery = true)
    Integer findMaxCodeByPrefix(String prefix);
}

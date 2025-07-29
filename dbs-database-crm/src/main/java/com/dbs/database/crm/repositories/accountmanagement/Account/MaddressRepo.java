package com.dbs.database.crm.repositories.accountmanagement.Account;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_ADDRESSES;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MaddressRepo extends PagingAndSortingRepository<M_ADDRESSES, Integer>, JpaSpecificationExecutor<M_ADDRESSES> {
    
    default Specification<M_ADDRESSES> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_ADDRESSES> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_ADDRESSES>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_ADDRESSES>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }

        return specification;
    }

    default Specification<M_ADDRESSES> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_ADDRESSES> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_ADDRESSES> addDefaultFilters(Specification<M_ADDRESSES> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        if(filter.get("serviceType") != null) {
            specification = (Specification<M_ADDRESSES>) PagingUtils.createSpecification("address1~" + (filter.get("address1")),DEFAULT_SELECTOR);
        }
        return specification;
    } 
    
    M_ADDRESSES findByAddressId(Integer addressId);
    
    Optional<M_ADDRESSES> findByFullAddress(String fullAddress);
    
    List<M_ADDRESSES> findAllByFullAddress(String fullAddress);
}

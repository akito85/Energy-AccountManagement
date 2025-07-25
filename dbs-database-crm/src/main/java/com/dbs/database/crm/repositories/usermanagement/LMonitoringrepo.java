package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.common.base.utils.GenericSpesification;
import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.LOG_MONITORING_SESSION;
import com.dbs.database.crm.entities.usermanagement.M_EMPLOYEE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LMonitoringrepo extends PagingAndSortingRepository <LOG_MONITORING_SESSION, Integer>, JpaSpecificationExecutor<LOG_MONITORING_SESSION> {

    default Specification<LOG_MONITORING_SESSION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<LOG_MONITORING_SESSION> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<LOG_MONITORING_SESSION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<LOG_MONITORING_SESSION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<LOG_MONITORING_SESSION> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }


    @Autowired
    public List<LOG_MONITORING_SESSION> findAll();

    void deleteByUserId(Integer userId);
    
    Optional<LOG_MONITORING_SESSION>findByUsernameAndIsDeleted(String username, Boolean isDeleted);
    
    List<LOG_MONITORING_SESSION>findAllByUsernameAndIsDeleted(String username, Boolean isDeleted);
    
    void deleteByUsernameAndIsDeleted(String username, Boolean isDeleted);
    
}

package com.dbs.database.crm.repositories.accountmanagement;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.accountmanagement.M_GAS_SOURCE;
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
public interface MGasSourceRepo extends PagingAndSortingRepository<M_GAS_SOURCE, Integer>, JpaSpecificationExecutor<M_GAS_SOURCE> {
    Optional<M_GAS_SOURCE> findTopByCalorieCodeIgnoreCase(String calorieCode);
    Optional<M_GAS_SOURCE> findTopByNameIgnoreCase(String name);
    List<M_GAS_SOURCE> findAllByCalorieCode(String calorieCode);
    Optional<M_GAS_SOURCE> findByGasSourceId(Integer gasSourceId);
    List<M_GAS_SOURCE> findAll();
}

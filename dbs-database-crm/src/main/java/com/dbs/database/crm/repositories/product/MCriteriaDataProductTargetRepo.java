package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_CRITERIA_DATA_PRODUCT_TARGET;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MCriteriaDataProductTargetRepo extends PagingAndSortingRepository<M_CRITERIA_DATA_PRODUCT_TARGET,String>, JpaSpecificationExecutor<M_CRITERIA_DATA_PRODUCT_TARGET> {

    Page<M_CRITERIA_DATA_PRODUCT_TARGET> findAllByProductTargetId(Integer id, Pageable var1);

    M_CRITERIA_DATA_PRODUCT_TARGET findTopById(Integer id);
}

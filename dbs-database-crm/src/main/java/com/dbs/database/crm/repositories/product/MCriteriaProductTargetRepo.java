package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_CRITERIA_PRODUCT_TARGET;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MCriteriaProductTargetRepo extends PagingAndSortingRepository<M_CRITERIA_PRODUCT_TARGET,String>, JpaSpecificationExecutor<M_CRITERIA_PRODUCT_TARGET> {

    List<M_CRITERIA_PRODUCT_TARGET> findAllByProductTargetId(Integer id);
}

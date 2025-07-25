package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.M_PRODUCT_TERM_OF_SERVICE;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MProductTermOfServiceRepo extends PagingAndSortingRepository<M_PRODUCT_TERM_OF_SERVICE,String>, JpaSpecificationExecutor<M_PRODUCT_TERM_OF_SERVICE> {

    M_PRODUCT_TERM_OF_SERVICE findTopById(Integer id);

    Page<M_PRODUCT_TERM_OF_SERVICE> findAllByProductVersionId(Integer id, Pageable var1);
    
    List<M_PRODUCT_TERM_OF_SERVICE> findAllByProductVersionId(Integer id);
    
    @Query("SELECT a FROM M_PRODUCT_TERM_OF_SERVICE a WHERE a.productVersionId = :productId AND a.id NOT IN :detailIds")
    List<M_PRODUCT_TERM_OF_SERVICE> findNotIn(Integer productId, List<Integer> detailIds);
}

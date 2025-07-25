package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.R_PRODUCT_BUNDLING;

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
public interface RProductBundlingRepo extends PagingAndSortingRepository<R_PRODUCT_BUNDLING,Integer>, JpaSpecificationExecutor<R_PRODUCT_BUNDLING> {

    R_PRODUCT_BUNDLING findTopById(Integer id);

    Page<R_PRODUCT_BUNDLING> findAllByProductVersionMainId(Integer id, Pageable var1);
    
    List<R_PRODUCT_BUNDLING> findAllByProductVersionMainId(Integer id);
    
    @Query("SELECT a FROM R_PRODUCT_BUNDLING a WHERE a.productVersionMainId = :productId AND a.id NOT IN :detailIds")
    List<R_PRODUCT_BUNDLING> findNotIn(Integer productId, List<Integer> detailIds);

    @Query(
            value = "SELECT COUNT(MPV.PRODUCT_ID) FROM R_PRODUCT_BUNDLING REP INNER JOIN M_PRODUCT_VERSION MPV ON REP.PRODUCT_VERSION_MAIN_ID=MPV.ID WHERE MPV.STATUS = 'ACTIVE' AND REP.PRODUCT_ID = ?1",
            nativeQuery = true)
    Integer findActiveProduct(Integer productId);
}

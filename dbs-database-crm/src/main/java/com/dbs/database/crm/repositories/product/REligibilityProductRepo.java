package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.R_ELIGIBILITY_PRODUCT;

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
public interface REligibilityProductRepo extends PagingAndSortingRepository<R_ELIGIBILITY_PRODUCT,String>, JpaSpecificationExecutor<R_ELIGIBILITY_PRODUCT> {

    R_ELIGIBILITY_PRODUCT findTopById(Integer id);

    Page<R_ELIGIBILITY_PRODUCT> findAllByProductVersionMainId(Integer id, Pageable var1);
    
    List<R_ELIGIBILITY_PRODUCT> findAllByProductVersionMainId(Integer id);
    
    @Query("SELECT a FROM R_ELIGIBILITY_PRODUCT a WHERE a.productVersionMainId = :productId AND a.id NOT IN :detailIds")
    List<R_ELIGIBILITY_PRODUCT> findNotIn(Integer productId, List<Integer> detailIds);

    @Query(
            value = "SELECT COUNT(MPV.PRODUCT_ID) FROM R_ELIGIBILITY_PRODUCT REP INNER JOIN M_PRODUCT_VERSION MPV ON REP.PRODUCT_VERSION_MAIN_ID=MPV.ID WHERE MPV.STATUS = 'ACTIVE' AND REP.PRODUCT_ID = ?1",
            nativeQuery = true)
    Integer findActiveProduct(Integer productId);

}

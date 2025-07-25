package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_TAX_CODE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MRbiTaxCodeRepo extends PagingAndSortingRepository<M_RBI_TAX_CODE, Integer>, JpaSpecificationExecutor<M_RBI_TAX_CODE> {

    List<M_RBI_TAX_CODE> findAllByCategory(Integer cat);

    @Query("SELECT a.taxCodeName FROM M_RBI_TAX_CODE a WHERE a.taxCode = :refId ")
    Optional<String> findTaxCodeName(String refId);
    
    @Query("SELECT u FROM M_RBI_TAX_CODE u WHERE LOWER(u.taxCode) = LOWER(:taxCode)")
    Optional<M_RBI_TAX_CODE> findTaxCodeIgnoreCase(@Param("taxCode") String taxCode);

    Optional<M_RBI_TAX_CODE> findFirstByTaxCode(String id);

    Optional<M_RBI_TAX_CODE> findByTaxCodeNameIgnoreCase (String codeName);
    
    Optional<M_RBI_TAX_CODE> findByTaxCodeIgnoreCase(String taxCode);

    @Autowired
    public List<M_RBI_TAX_CODE> findAll();
    
    public List<M_RBI_TAX_CODE> findAllByStatus(String status);
}

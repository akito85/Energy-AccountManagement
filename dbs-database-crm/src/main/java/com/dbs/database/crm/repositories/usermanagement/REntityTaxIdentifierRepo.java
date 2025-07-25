package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_ENTITY_TAX_IDENTIFIER;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface REntityTaxIdentifierRepo extends JpaRepository<R_ENTITY_TAX_IDENTIFIER,String> {

    Optional<R_ENTITY_TAX_IDENTIFIER> findTopByTaxId(Integer id);

    List<R_ENTITY_TAX_IDENTIFIER> findByentityIdAndStatus(Integer entityId, String status);

    @Query("SELECT a FROM R_ENTITY_TAX_IDENTIFIER a WHERE a.entityId = :entityId AND a.taxId NOT IN :taxId")
    List<R_ENTITY_TAX_IDENTIFIER> findNotIn(Integer entityId, List<Integer> taxId);
    
    List<R_ENTITY_TAX_IDENTIFIER> findByTaxNumberAndStatus(String taxNumber, String status);
}

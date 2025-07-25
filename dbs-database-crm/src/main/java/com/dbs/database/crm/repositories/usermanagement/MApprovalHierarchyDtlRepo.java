package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.M_APPROVAL_HIERARCHY_DTL;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MApprovalHierarchyDtlRepo extends JpaRepository<M_APPROVAL_HIERARCHY_DTL, Integer> {
    @Query("SELECT a FROM M_APPROVAL_HIERARCHY_DTL a WHERE a.appHierId = :appHierId AND a.appHierDtlId NOT IN :appHierDtlId")
    List<M_APPROVAL_HIERARCHY_DTL> findNotIn(Integer appHierId, List<Integer> appHierDtlId);
    Optional<M_APPROVAL_HIERARCHY_DTL> findFirstByAppHierIdAndIsFinal(Integer appHierId, Boolean isFinal);

    Optional<M_APPROVAL_HIERARCHY_DTL> findFirstByAppHierIdAndIsSubmitter(Integer appHierId, Boolean isSubmitter);

    Optional<M_APPROVAL_HIERARCHY_DTL> findByAppHierDtlId(Integer positionId);

    List<M_APPROVAL_HIERARCHY_DTL> findByAppHierIdAndStatus(Integer appHierId, String status);
    
    List<M_APPROVAL_HIERARCHY_DTL> findByAppHierIdAndStatus(Integer appHierId, String status, Sort sort);
    
    Optional<M_APPROVAL_HIERARCHY_DTL> findFirstByAppHierIdAndApprovalLevel(Integer appHierId, Integer lvl);
    List<M_APPROVAL_HIERARCHY_DTL> findByAppHierId(Integer appHier);

    @Query(value = "SELECT mahd.APPHIER_DTL_ID FROM M_APPROVAL_HIERARCHY_DTL mahd \n" +
            "WHERE mahd.POSITION_ID=:positionId AND mahd.IS_SUBMITTER = 'N'\n" +
            "AND EXISTS \n" +
            "\t(\n" +
            "\t\tSELECT 1 FROM (SELECT APPHIER_ID FROM M_APPROVAL_HIERARCHY WHERE STATUS = 'ACTIVE' AND lower(APPROVAL_TYPE) = 'payment' AND APPHIER_ID=mahd.APPHIER_ID) maph \n" +
            "\t\tINNER JOIN \n" +
            "\t\t\t(\n" +
            "\t\t\t\tSELECT a.APPHIER_ID, a.APPHIER_DTL_ID, a.POSITION_ID FROM M_APPROVAL_HIERARCHY_DTL a\n" +
            "\t\t\t\tWHERE a.IS_SUBMITTER = 'Y' \n" +
            "\t\t\t\tAND EXISTS \n" +
            "\t\t\t\t\t(\n" +
            "\t\t\t\t\t\tSELECT 1 FROM VW_EMPLOYEE_ASSIGNMENT vea \n" +
            "\t\t\t\t\t\tWHERE EXISTS \n" +
            "\t\t\t\t\t\t\t(\n" +
            "\t\t\t\t\t\t\t\tSELECT 1 FROM (SELECT EMPLOYEE_ID FROM M_USER WHERE USERNAME=:username) mu \n" +
            "\t\t\t\t\t\t\t\tINNER JOIN (SELECT EMPLOYEE_ID FROM M_EMPLOYEE WHERE EMPLOYEE_CODE=vea.EMPLOYEE_CODE) me ON mu.EMPLOYEE_ID=me.EMPLOYEE_ID \n" +
            "\t\t\t\t\t\t\t)\n" +
            "\t\t\t\t\t\tAND vea.POSITION_ID = a.POSITION_ID\n" +
            "\t\t\t\t\t)\n" +
            "\t\t\t) maphd ON maph.APPHIER_ID = maphd.APPHIER_ID\n" +
            "\t)", nativeQuery = true)
    List<Integer> findByPositionIdAndIsSubmitter(Integer positionId, String username);
    
    Optional<M_APPROVAL_HIERARCHY_DTL> findFirstByAppHierDtlIdAndIsFinal(Integer appHierDtlId, Boolean isFinal);
    
    Optional<M_APPROVAL_HIERARCHY_DTL> findFirstByAppHierDtlIdAndIsSubmitter(Integer appHierDtlId, Boolean isSubmitter);

    @Query(value = "SELECT mahd.* FROM M_APPROVAL_HIERARCHY mah INNER JOIN M_APPROVAL_HIERARCHY_DTL mahd ON MAH.APPHIER_ID = MAHD.APPHIER_ID WHERE mah.APPROVAL_TYPE = :approvalType", nativeQuery = true)
    List<M_APPROVAL_HIERARCHY_DTL> findByApprovalType(String approvalType);

}

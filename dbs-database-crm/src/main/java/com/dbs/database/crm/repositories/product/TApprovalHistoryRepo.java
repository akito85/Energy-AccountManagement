package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.T_APPROVAL_HISTORY;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.List;

@Repository
@Transactional(value = "crmTransactionManager")
public interface TApprovalHistoryRepo extends JpaRepository<T_APPROVAL_HISTORY, Integer> {

	Optional<T_APPROVAL_HISTORY> findFirstBytAppId(Integer tAppId);


	Optional<List<T_APPROVAL_HISTORY>> findByRefIdAndCategoryAndApprovalType(Integer refId, String cat, String type, Sort sort);
	Optional<List<T_APPROVAL_HISTORY>> findByRefIdStringAndCategoryAndApprovalType(String refIdString, String cat, String type, Sort sort);

	Optional<List<T_APPROVAL_HISTORY>> findByRefIdAndCategory(Integer refId, String cat);
	Optional<List<T_APPROVAL_HISTORY>> findByRefIdStringAndCategory(String refIdString, String cat);

	Optional<List<T_APPROVAL_HISTORY>> findBytAppIdAndPositionId(Integer tappid, Integer posId);

	T_APPROVAL_HISTORY findBytAppIdAndIsSubmitter(Integer tappId, Boolean sub);

	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.eventType ='SUBMIT'")
	Integer findTopSubmittedByRefId(Integer refId);

	@Query("SELECT MAX(a.approvalLevel) FROM T_APPROVAL_HISTORY a WHERE a.tAppId= :tappId")
	Optional<Integer> findTopApprovalLevelByTAppId(Integer tappId);
	
	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.eventType ='SUBMIT' AND a.category = :cat AND a.approvalType = :type")
	Integer findTopSubmittedByRefIdAndCategoryAndType(Integer refId, String cat, String type);
	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND (a.eventType ='APPROVE' OR a.eventType ='REJECT') AND a.category = :cat AND a.approvalType = :type")
	Integer findTopSubmittedByRefIdAndCategoryAndTypeForApprove(Integer refId, String cat, String type);
	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refIdString = :refIdString AND a.eventType ='SUBMIT' AND a.category = :cat AND a.approvalType = :type")
	Integer findTopSubmittedByRefIdAndCategoryAndType(String refIdString, String cat, String type);
	
	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.eventType ='SUBMIT' AND a.category = :cat")
	Integer findTopSubmittedByRefIdAndCategory(@Param("refId") Integer refId, @Param("cat")String cat);
	@Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refIdString = :refIdString AND a.eventType ='SUBMIT' AND a.category = :cat")
	Integer findTopSubmittedByRefIdStrAndCategory(@Param("refIdString") String refIdString, @Param("cat")String cat);

	@Query("SELECT a FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.approvalLevel >= 1 AND a.tAppId >= :approvalId")
	Optional<List<T_APPROVAL_HISTORY>> findAllApprover(Integer refId, Integer approvalId);
	
	@Query("SELECT a FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.approvalLevel >= 1 AND a.tAppId >= :approvalId AND a.category = :cat AND a.approvalType = :type")
	Optional<List<T_APPROVAL_HISTORY>> findAllApproverByCategoryAndType(Integer refId, Integer approvalId, String cat, String type);
	@Query("SELECT a FROM T_APPROVAL_HISTORY a WHERE a.refIdString = :refIdString AND a.approvalLevel >= 1 AND a.tAppId >= :approvalId AND a.category = :cat AND a.approvalType = :type")
	Optional<List<T_APPROVAL_HISTORY>> findAllApproverByCategoryAndTypeRefString(String refIdString, Integer approvalId, String cat, String type);
	@Query("SELECT a FROM T_APPROVAL_HISTORY a WHERE a.refId =:refId AND a.category IN (:categoryList)")
	List<T_APPROVAL_HISTORY> findAllByRefIdAndCategory(Integer refId, List<String> categoryList);
        @Query("SELECT MAX(a.tAppId) FROM T_APPROVAL_HISTORY a WHERE a.refId = :refId AND a.eventType ='SUBMIT' AND a.approvalType = :approvalType")
	Integer findTopSubmittedByRefIdAndApprovalType(@Param("refId") Integer refId, @Param("approvalType")String approvalType);

}

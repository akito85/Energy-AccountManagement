package com.dbs.database.crm.repositories.product;

import com.dbs.database.crm.entities.product.VW_APPROVAL_HIERARCHY_DETAIL;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWApprovalHierarchyDtlRepo extends PagingAndSortingRepository<VW_APPROVAL_HIERARCHY_DETAIL, String>,
		JpaSpecificationExecutor<VW_APPROVAL_HIERARCHY_DETAIL> {

	@Query(value = "SELECT u FROM VW_APPROVAL_HIERARCHY_DETAIL u WHERE u.apphierId = :appHierId GROUP BY u.approvalLevel")
	List<VW_APPROVAL_HIERARCHY_DETAIL> findHierarchyById(@Param("appHierId") Integer appHierId);

	@Query(value = "SELECT distinct u.position FROM VW_APPROVAL_HIERARCHY_DETAIL u WHERE u.apphierId = :appHierId ")
	List<String> findPositionById(@Param("appHierId") Integer appHierId);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierId(Integer id);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierIdAndPositionId(Integer id, Integer position);

	Optional<VW_APPROVAL_HIERARCHY_DETAIL> findByApphierIdAndPositionIdAndApprovalLevel(Integer id, Integer position,
			Integer lvl);

	Optional<VW_APPROVAL_HIERARCHY_DETAIL> findFirstByApphierIdAndPositionIdAndApprovalLevelAndEmployeeId(Integer id,
			Integer position, Integer lvl, Integer empId);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierIdAndApprovalLevelAndPositionIdAndIsSubmitterAndIsFinal(
			Integer id, Integer lvl, Integer position, String sub, String fin);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierIdAndPositionIdAndIsSubmitterAndIsFinal(Integer id,
			Integer position, String sub, String fin);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierIdAndPositionAndIsSubmitterAndIsFinal(Integer id,
			String position, String sub, String fin);

	Optional<List<VW_APPROVAL_HIERARCHY_DETAIL>> findByApphierIdAndApprovalLevelAndPositionAndIsSubmitterAndIsFinal(
			Integer id, Integer lvl, String position, String sub, String fin);

	List<VW_APPROVAL_HIERARCHY_DETAIL> findAllByApphierId(Integer apphierId);
}

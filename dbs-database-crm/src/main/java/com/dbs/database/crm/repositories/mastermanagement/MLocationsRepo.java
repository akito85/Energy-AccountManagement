package com.dbs.database.crm.repositories.mastermanagement;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static com.dbs.common.base.utils.Constant.EQUALS_SELECTOR;
import com.dbs.database.crm.entities.product.M_LOCATION;
import static org.springframework.data.jpa.domain.Specification.where;


@Repository
@Transactional(value = "crmTransactionManager")
public interface MLocationsRepo extends PagingAndSortingRepository<M_LOCATION, Integer>, JpaSpecificationExecutor<M_LOCATION> {

	default Specification<M_LOCATION> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
		Specification<M_LOCATION> specification = null;
		//Add Filter From Front End
		int i = 0;
		for (String sr : pagingdata.getSearch()) {
			specification =
					i == 0 ?
							(Specification<M_LOCATION>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
							: specification.and((Specification<M_LOCATION>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
			i++;
		}
		/*------------------------------------------*/
		/*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
		specification = addDefaultFilters(specification, filter, false);
		/*code here*/

		/*------------------------------------------*/
		/*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
		/*code here*/

		return specification;
	}

	default Specification<M_LOCATION> getSpecificationDefault(Map<String, Object> filter) {
		Specification<M_LOCATION> specification = null;
		specification = addDefaultFilters(specification, filter, true);
		return specification;
	}

	default Specification<M_LOCATION> addDefaultFilters(Specification<M_LOCATION> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
            if(filter.get("locationTypeId") != null) {
                if(specification == null) {
                    specification =(Specification<M_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR);
                } else {
                    specification =specification.and((Specification<M_LOCATION>) PagingUtils.createSpecification("locationTypeId~" + filter.get("locationTypeId"), EQUALS_SELECTOR));
                }
            }
            if(filter.get("locationParent") != null) {
                if(specification == null) {
                    specification =(Specification<M_LOCATION>) PagingUtils.createSpecification("locationParent~" + filter.get("locationParent"), EQUALS_SELECTOR);
                } else {
                    specification =specification.and((Specification<M_LOCATION>) PagingUtils.createSpecification("locationParent~" + filter.get("locationParent"), EQUALS_SELECTOR));
                }
            }
            return specification;
        }

    List<M_LOCATION> findAllByLocationType(String locationType);

    M_LOCATION findByLocationId (Integer locationId);

    List<M_LOCATION> findAllByLocationTypeAndLocationParent(String locationType, Integer locationParent);

    Optional<M_LOCATION> findByLocationIdAndLocationType(Integer locationId, String locationType);
    Optional<M_LOCATION> findByLocationIdAndLocationTypeId(Integer locationId, Integer locationTypeId);
    List<M_LOCATION> findAllByLocationTypeId(Integer locationTypeId);
    List<M_LOCATION> findAllByLocationParent(Integer locationTypeId);

    List<M_LOCATION> findAllByLocationNameAndLocationTypeIdAndLocationParent(String locationName, Integer locationTypeId, Integer locationParent);

    List<M_LOCATION> findAllByLocationTypeIdAndStatus(Integer locationTypeId, String status);
    
    Optional<M_LOCATION> findByLocationTypeAndLocationCodeAndLocationParent(String locationType, String locationCode, Integer locationParent);

}
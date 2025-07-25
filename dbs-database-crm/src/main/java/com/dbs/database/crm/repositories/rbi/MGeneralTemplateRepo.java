package com.dbs.database.crm.repositories.rbi;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.ratingbillinginvoice.M_GENERAL_TEMPLATE;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface MGeneralTemplateRepo extends PagingAndSortingRepository<M_GENERAL_TEMPLATE, Integer>, JpaSpecificationExecutor<M_GENERAL_TEMPLATE> {

    default Specification<M_GENERAL_TEMPLATE> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<M_GENERAL_TEMPLATE> specification = null;
        //Add Filter From Front End
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<M_GENERAL_TEMPLATE>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<M_GENERAL_TEMPLATE>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        /*------------------------------------------*/
        /*INFO: Add Default Filter, ex: Entity, Cost Center, IsDeleted, etc.*/
        specification = addDefaultFilters(specification, filter,false);
        /*code here*/

        /*------------------------------------------*/
        /*INFO: Add Addition Filter If Any, ex: filter with Join Column.*/
        /*code here*/

        return specification;
    }

    default Specification<M_GENERAL_TEMPLATE> getSpecificationDefault(Map<String, Object> filter) {
        Specification<M_GENERAL_TEMPLATE> specification = null;
        specification = addDefaultFilters(specification, filter, true);
        return specification;
    }

    default Specification<M_GENERAL_TEMPLATE> addDefaultFilters(Specification<M_GENERAL_TEMPLATE> specification, Map<String, Object> filter, Boolean isFirst){
        /*INFO: Add Entity Filter*/
        specification = (Specification<M_GENERAL_TEMPLATE>) PagingUtils.createEntityFilter(specification, Integer.parseInt(filter.get("entityId").toString()), isFirst);
        /*INFO: Add Cost Center Filter*/
//        List<String> tes = null;//Get Cost Center From Username To Be Done
        return specification;
    }

    List<M_GENERAL_TEMPLATE> findAllByStatus(String status);

    Optional<M_GENERAL_TEMPLATE> findByTemplateId (Integer id);

    Optional<M_GENERAL_TEMPLATE> findByTemplateNameIgnoreCase(String templateName);

    @Query(nativeQuery = true, value = "SELECT * FROM M_GENERAL_TEMPLATE mgt \n" +
            "WHERE MGT.TEMPLATE_NAME = LOWER(:templateName)")
    Optional<M_GENERAL_TEMPLATE> findByNameLowerCase (String templateName);

    Optional<M_GENERAL_TEMPLATE> findByTemplateName(String templateName);

    List<M_GENERAL_TEMPLATE> findAll();
}

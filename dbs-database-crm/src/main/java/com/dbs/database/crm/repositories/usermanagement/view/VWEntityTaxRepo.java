package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import com.dbs.database.crm.entities.usermanagement.view.VW_ENTITY_TAX;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import java.util.List;
import static org.springframework.data.jpa.domain.Specification.where;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWEntityTaxRepo extends PagingAndSortingRepository<VW_ENTITY_TAX, Integer>, JpaSpecificationExecutor<VW_ENTITY_TAX> {
    default Specification<VW_ENTITY_TAX> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_ENTITY_TAX> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            String[] searchs = sr.trim().split("~");
            if (searchs.length > 0 && searchs[0].equals("taxIdentifier")) {
                sr = "taxIdentifier~"+searchs[1].replace(".", "").replace("-", "");
            }
            specification =
                    i == 0 ?
                            (Specification<VW_ENTITY_TAX>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_ENTITY_TAX>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<VW_ENTITY_TAX> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }

    @Override
    public List<VW_ENTITY_TAX> findAll();
    
}

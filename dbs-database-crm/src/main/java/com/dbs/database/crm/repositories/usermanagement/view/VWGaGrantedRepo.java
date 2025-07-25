package com.dbs.database.crm.repositories.usermanagement.view;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.common.base.utils.PagingUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.dbs.common.base.utils.Constant.DEFAULT_SELECTOR;
import com.dbs.database.crm.entities.usermanagement.view.VW_GA_GRANTED;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.jpa.domain.Specification.where;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional(value = "crmTransactionManager")
public interface VWGaGrantedRepo extends PagingAndSortingRepository<VW_GA_GRANTED, Integer>, JpaSpecificationExecutor<VW_GA_GRANTED> {
    default Specification<VW_GA_GRANTED> getSpecificationFromFilters(MaterialTablePagingRequest pagingdata, Map<String, Object> filter) {
        Specification<VW_GA_GRANTED> specification = null;
        int i = 0;
        for (String sr : pagingdata.getSearch()) {
            specification =
                    i == 0 ?
                            (Specification<VW_GA_GRANTED>) where(PagingUtils.createSpecification(sr, DEFAULT_SELECTOR))
                            : specification.and((Specification<VW_GA_GRANTED>) PagingUtils.createSpecification(sr, DEFAULT_SELECTOR));
            i++;
        }
        return specification;
    }

    default Specification<VW_GA_GRANTED> getSpecificationDefault(Map<String, Object> filter) {
        return null;
    }
    
    Optional<List<VW_GA_GRANTED>> findByUserIdAndGaId(Integer userId, Integer gaId);
    
    Optional<List<VW_GA_GRANTED>> findByPath(String path);
    
    
    @Query(nativeQuery = true, value = "SELECT * FROM VW_GA_GRANTED a WHERE a.USER_ID = :userId AND a.GA_ID = :gaId AND a.PATH LIKE %:path%")
    Optional<List<VW_GA_GRANTED>> findByUserIdAndGaIdAndPath(Integer userId, Integer gaId, String path);
    @Query(nativeQuery = true, value = "SELECT * FROM VW_GA_GRANTED a WHERE a.USER_ID = :userId AND a.GA_ID = :gaId AND a.PATH LIKE %:path% AND a.NAME = :view")
    Optional<List<VW_GA_GRANTED>> findByUserIdAndGaIdAndPathAndView(Integer userId, Integer gaId, String path, String view);
    
    @Query(nativeQuery = true, value = "SELECT * FROM VW_GA_GRANTED a WHERE a.USER_ID = :userId AND a.GA_ID IN(:gaId) AND a.PATH LIKE %:path%")
    Optional<List<VW_GA_GRANTED>> findByUserIdAndGaIdsAndPath(Integer userId, List<Integer> gaId, String path);
}

package com.dbs.database.crm.repositories.usermanagement;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(value = "crmTransactionManager")
public interface RGlobalTypeValueRepo extends JpaRepository<R_GLOBAL_TYPE_VALUE, Integer> {
    @Override
    Optional<R_GLOBAL_TYPE_VALUE> findById(Integer integer);

    R_GLOBAL_TYPE_VALUE findTopByGlbTypeValIdAndIsDeleted(Integer glbTypeValId, Boolean isDeleted);

    R_GLOBAL_TYPE_VALUE findTopByGlbValueIgnoreCaseAndIsDeleted(String glbValue, Boolean isDeleted);

    List<R_GLOBAL_TYPE_VALUE> findByGlobalType(Integer globalType);
    Optional<R_GLOBAL_TYPE_VALUE> findByGlobalTypeAndName(Integer globalType, String name);

    Optional<List<R_GLOBAL_TYPE_VALUE>> findByName(String name);

    Optional<R_GLOBAL_TYPE_VALUE> findByNameAndGlobalType(String name, Integer glbTypeId);

    Optional<R_GLOBAL_TYPE_VALUE> findByGlbTypeValIdAndGlobalType(Integer glbTypeValId, Integer globalType);

    Optional<R_GLOBAL_TYPE_VALUE> findByGlbTypeValId(Integer glbTypeValId);

    Optional<R_GLOBAL_TYPE_VALUE> findByGlbValue (String glbValue);

    List<R_GLOBAL_TYPE_VALUE> findAllByGlobalType(Integer globalType);

    List<R_GLOBAL_TYPE_VALUE> findAllByGlobalTypeAndStatus(Integer globalType, String status);

    List<R_GLOBAL_TYPE_VALUE> findByParentValueAndIsDeleted(Integer parentValue, Boolean isDeleted);
    
    List<R_GLOBAL_TYPE_VALUE> findAllByParentValueAndIsDeleted(Integer parentValue, Boolean isDeleted);
    List<R_GLOBAL_TYPE_VALUE> findAllByParentValueAndIsDeletedAndStatus(Integer parentValue, Boolean isDeleted, String status);
    
    @Query("SELECT a FROM R_GLOBAL_TYPE_VALUE a WHERE a.globalType = :globalType AND a.glbTypeValId NOT IN :glbTypeValIds")
    List<R_GLOBAL_TYPE_VALUE> findNotIn(Integer globalType, List<Integer> glbTypeValIds);

    @Query(value = "SELECT rgtv.* FROM R_GLOBAL_TYPE_VALUE rgtv \n" +
            "WHERE EXISTS (SELECT 1 FROM M_GLOBAL_TYPE mgt WHERE mgt.GLB_TYPE_ID =84 AND mgt.GLB_TYPE_ID=rgtv.GLB_TYPE_ID AND mgt.STATUS = 'ACTIVE' AND mgt.IS_DELETED='N')" +
            "ORDER BY rgtv.GLB_TYPE_VAL_ID", nativeQuery = true)
    List<R_GLOBAL_TYPE_VALUE> findTimeUnit();
    Optional<R_GLOBAL_TYPE_VALUE> findTopByGlobalTypeAndGlbValue(Integer glbTypeId,String globalValue);
    Optional<R_GLOBAL_TYPE_VALUE> findByGlobalTypeAndGlbValueIgnoreCase(Integer glbTypeId,String globalValue);

    @Query(value = "SELECT rgtv.GLB_TYPE_VAL_ID, rgtv.NAME FROM R_GLOBAL_TYPE_VALUE rgtv \n" +
            "WHERE EXISTS (SELECT 1 FROM M_GLOBAL_TYPE mgt WHERE mgt.GLB_TYPE_ID=242 AND mgt.GLB_TYPE_ID = rgtv.GLB_TYPE_ID)", nativeQuery = true)
    List<Tuple> findCurrencyList();

    @Query(value = "SELECT GLB_VALUE FROM R_GLOBAL_TYPE_VALUE WHERE GLB_TYPE_VAL_ID =:glbTypeValId AND IS_DELETED='N' AND STATUS ='ACTIVE'", nativeQuery = true)
    Optional<String> findGlbValueByGlbTypeValId(Integer glbTypeValId);

    @Query(value = "SELECT NVL(RGTv.NAME, '-') name FROM R_GLOBAL_TYPE_VALUE rgtv \n" +
            "WHERE RGTV.GLB_TYPE_VAL_ID =:id AND EXISTS (SELECT 1 FROM M_GLOBAL_TYPE mgt WHERE mgt.GROUPNAME =:groupName AND STATUS = 'ACTIVE' AND mgt.GLB_TYPE_ID = rgtv.GLB_TYPE_ID)", nativeQuery = true)
    String findCustomByGlAndGlbTypeValId(Integer id, String groupName);

    @Query(value = "SELECT a.GLB_TYPE_VAL_ID, a.NAME FROM R_GLOBAL_TYPE_VALUE a  \n" +
            "WHERE EXISTS (SELECT 1 FROM M_GLOBAL_TYPE b WHERE b.GROUPNAME =:groupName AND b.GLB_TYPE_ID = a.GLB_TYPE_ID)", nativeQuery = true)
    List<Object[]> findAllByGroupName(String groupName);

    @Query(value = "SELECT NAME FROM R_GLOBAL_TYPE_VALUE WHERE GLB_TYPE_VAL_ID =:id", nativeQuery = true)
    Optional<String> findNameById(Integer id);

    List<R_GLOBAL_TYPE_VALUE> findAllByGlobalTypeAndParentValue(Integer globalType, Integer parentValue);

    Boolean existsByParentValue(Integer parentValue);

}

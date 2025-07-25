package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ChooseContactRepoCustom implements ChooseContactInterface {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Page<VW_CHOOSE_CONTACT> findByContactIdIn(String type,Specification<VW_CHOOSE_CONTACT> specification, MaterialTablePagingRequest pagingData, Pageable pageable, List<Integer> contactIds, Class<VW_CHOOSE_CONTACT> entityClass) {

        Map<String, String> fieldToColumnMap = new HashMap<>();
        fieldToColumnMap.put("createdDate", "created_date");
        fieldToColumnMap.put("contactName", "contact_name");
        fieldToColumnMap.put("jobName", "job_name");
        fieldToColumnMap.put("positionName", "position_name");

        StringBuilder sql = new StringBuilder(
                "SELECT vw.*, \n"
        );

        if(!contactIds.isEmpty()) {
            sql.append(
                    "       CASE \n" +
                            "           WHEN vw.CONTACT_ID IN (:contactIds) THEN '" + type.toUpperCase() + "'\n" +
                            "           ELSE 'MASTER'\n" +
                            "       END AS \"SOURCE\"\n"
            );
        } else {
            sql.append("'MASTER' AS \"SOURCE\"\n");
        }

        StringBuilder sqlSize = new StringBuilder("SELECT COUNT(*)");
        String sqlBase = "FROM VW_CHOOSE_CONTACT vw WHERE vw.STATUS = 'ACTIVE' ";

        sql.append(sqlBase);
        sqlSize.append(sqlBase);

        // SEARCHING
        if (pagingData.getSearch() != null && !pagingData.getSearch().isEmpty()) {
            for (String searchParam : pagingData.getSearch()) {
                String[] parts = searchParam.split("~");
                String columnName = parts[0];
                String value = parts[1];
                String column = fieldToColumnMap.getOrDefault(columnName, columnName);
                if(column.equalsIgnoreCase("source") && !contactIds.isEmpty()){
                    if(value.equalsIgnoreCase(type.toUpperCase())){
                        sql.append("AND vw.CONTACT_ID IN (:contactIds)");
                        sqlSize.append("AND vw.CONTACT_ID IN (:contactIds)");
                    } else if(value.equalsIgnoreCase("MASTER")){
                        sql.append("AND vw.CONTACT_ID NOT IN (:contactIds)");
                        sqlSize.append("AND vw.CONTACT_ID NOT IN (:contactIds)");
                    }
                } else if(!column.equalsIgnoreCase("source"))  {
                    sql.append(" AND UPPER(").append(column).append(") LIKE UPPER('%").append(value).append("%')");
                    sqlSize.append(" AND UPPER(").append(column).append(") LIKE UPPER('%").append(value).append("%')");
                }

//                //SEARCH ACTIVE OR INACTIVE
//                sql.append(" AND ").append(column).append(
//                        columnName.equalsIgnoreCase("status") ?
//                                " = " : " LIKE '%").append(value).append( columnName.equalsIgnoreCase("status") ?
//                        "" : "%'");
            }
        }

        // SORTING
        if (pageable.getSort().isSorted()) {
            sql.append(" ORDER BY ");
            List<String> sortingFields = pageable.getSort().stream()
                    .map(order -> {
                        String columnName = order.getProperty();
                        String column = fieldToColumnMap.getOrDefault(columnName, columnName);
                        return column + " " + order.getDirection().name();
                    })
                    .collect(Collectors.toList());
            sql.append(String.join(", ", sortingFields));
        }

        Query query = entityManager.createNativeQuery(sql.toString(), entityClass);
        if(!contactIds.isEmpty()) {
            query.setParameter("contactIds", contactIds);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<VW_CHOOSE_CONTACT> resultList = query.getResultList();

        Query countQuery = entityManager.createNativeQuery(sqlSize.toString());
        if(sqlSize.toString().contains("IN") && !contactIds.isEmpty()) {
            countQuery.setParameter("contactIds", contactIds);
        }
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);

    }
}

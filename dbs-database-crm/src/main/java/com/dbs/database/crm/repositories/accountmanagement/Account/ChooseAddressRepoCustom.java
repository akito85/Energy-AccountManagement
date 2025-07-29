package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(value = "crmTransactionManager")
public class ChooseAddressRepoCustom implements ChooseAddressInterface {

    @Autowired
    @Qualifier("crmEntityManagerInstance")
    private EntityManager entityManager;

    @Override
    public Page<VW_CHOOSE_ADDRESS> findByAddressIdIn(String type, Specification<VW_CHOOSE_ADDRESS> specification, MaterialTablePagingRequest pagingData, Pageable pageable, List<Integer> addressIds, Class<VW_CHOOSE_ADDRESS> entityClass) {

        Map<String, String> fieldToColumnMap = new HashMap<>();
        fieldToColumnMap.put("createdDate", "created_date");
        fieldToColumnMap.put("fullAddress", "full_address");
        fieldToColumnMap.put("additionalNote", "additional_info");
        fieldToColumnMap.put("houseName", "house_name");
        fieldToColumnMap.put("streetName", "street_name");
        fieldToColumnMap.put("houseNumber", "house_number");
        fieldToColumnMap.put("rt", "neighborhood_1");
        fieldToColumnMap.put("rw", "neighborhood_2");
        fieldToColumnMap.put("postalCode", "postal_code");
        fieldToColumnMap.put("subDistrict", "sub_district");

        StringBuilder sql = new StringBuilder(
                "SELECT vw.*, \n"

        );

        if(!addressIds.isEmpty()) {
            sql.append(
                    "       CASE \n" +
                    "           WHEN vw.ADDRESS_ID IN (:addressIds) THEN '" + type.toUpperCase() + "'\n" +
                    "           ELSE 'MASTER'\n" +
                    "       END AS \"SOURCE\"\n"
            );
        } else {
            sql.append("'MASTER' AS \"SOURCE\"\n");
        }

        StringBuilder sqlSize = new StringBuilder("SELECT COUNT(*)");
        String sqlBase = "FROM VW_CHOOSE_ADDRESS vw WHERE vw.STATUS = 'ACTIVE' ";

        sql.append(sqlBase);
        sqlSize.append(sqlBase);

        // SEARCHING
        if (pagingData.getSearch() != null && !pagingData.getSearch().isEmpty()) {
            for (String searchParam : pagingData.getSearch()) {
                String[] parts = searchParam.split("~");
                String columnName = parts[0];
                String value = parts[1];
                String column = fieldToColumnMap.getOrDefault(columnName, columnName);
                if(column.equalsIgnoreCase("source") && !addressIds.isEmpty()) {
                    if(value.equalsIgnoreCase(type.toUpperCase())){
                        sql.append("AND vw.ADDRESS_ID IN (:addressIds)");
                        sqlSize.append("AND vw.ADDRESS_ID IN (:addressIds)");
                    } else if(value.equalsIgnoreCase("MASTER")){
                        sql.append("AND vw.ADDRESS_ID NOT IN (:addressIds)");
                        sqlSize.append("AND vw.ADDRESS_ID NOT IN (:addressIds)");
                    }
                } else if(!column.equalsIgnoreCase("source")) {
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
        if(!addressIds.isEmpty()) {
            query.setParameter("addressIds", addressIds);
        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<VW_CHOOSE_ADDRESS> resultList = query.getResultList();

        Query countQuery = entityManager.createNativeQuery(sqlSize.toString());
        if(sqlSize.toString().contains("IN") && !addressIds.isEmpty()) {
            countQuery.setParameter("addressIds", addressIds);
        }
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);

    }
}

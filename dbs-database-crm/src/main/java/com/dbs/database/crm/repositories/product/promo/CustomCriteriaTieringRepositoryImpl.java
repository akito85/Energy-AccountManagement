package com.dbs.database.crm.repositories.product.promo;

import com.dbs.database.crm.entities.product.promo.VW_PROMO_TIERING;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;


@Repository
public class CustomCriteriaTieringRepositoryImpl implements CustomCriteriaTieringRepository {
    @Autowired
    @Qualifier("crmEntityManagerInstance")
    private EntityManager em;

    @Override
    public List<VW_PROMO_TIERING> customTiering(Integer sor,
                                                Integer custSegment,
                                                Integer accCat,
                                                Integer subDistrict,
                                                Integer serviceType,
                                                Integer account,
                                                Integer budget,
                                                Integer accGroup,
                                                Integer industrialSec,
                                                Integer gSizes,
                                                Integer province,
                                                Integer district,
                                                Integer city,
                                                Integer costCenter) {
        String qsor = "";
        String qcustSegment = "";
        String qaccCat = "";
        String qsubDistrict = "";
        String qserviceType = "";
        String qaccount = "";
        String qbudget = "";
        String qaccGroup = "";
        String qindustrialSec = "";
        String qgSizes = "";
        String qprovince = "";
        String qdistrict = "";
        String qcity = "";
        String qcostCenter = "";

        if (sor != null) {
            qsor = " AND RPRCD.SOR = :sor";
        }
        if (custSegment != null) {
            qcustSegment = " AND RPRCD.CUSTOMER_SEGMENT = :custSegment";
        }
        if (accCat != null) {
            qaccCat = " AND RPRCD.ACCOUNT_CATEGORY = :accCat";
        }
        if (subDistrict != null) {
            qsubDistrict = " AND RPRCD.SUB_DISTRICT = :subDistrict";
        }
        if (serviceType != null) {
            qserviceType = " AND RPRCD.SERVICE_TYPE = :serviceType";
        }
        if (account != null) {
            qaccount = " AND RPRCD.CUSTOMER = :account";
        }
        if (budget != null) {
            qbudget = " AND RPRCD.BUDGET = :budget";
        }
        if (accGroup != null) {
            qaccGroup = " AND RPRCD.ACCOUNT_GROUP = :accGroup";
        }
        if (industrialSec != null) {
            qindustrialSec = " AND RPRCD.INDUSTRIAL_SECTOR = :industrialSec";
        }
        if (gSizes != null) {
            qgSizes = " AND RPRCD.GSIZES = :gSizes";
        }
        if (province != null) {
            qprovince = " AND RPRCD.PROVINCE = :province";
        }
        if (district != null) {
            qdistrict = " AND RPRCD.DISTRICT = :district";
        }
        if (city != null) {
            qcity = " AND RPRCD.CITY = :city";
        }
        if (costCenter != null) {
            qcostCenter = " AND RPRCD.AREA = :costCenter";
        }


        String queryString =
                "SELECT DISTINCT VPT.TIERING_ID, VPT.PRICING_RULE_ID, VPT.TIERING FROM VW_PROMO_TIERING vpt \n" +
                        "LEFT JOIN R_PRICING_RULE_CRITERIA_DATA rprcd ON RPRCD.ID_PRICING_RULE = VPT.PRICING_RULE_ID WHERE 1 = 1\n" +
                        qsor +
                        qcustSegment +
                        qaccCat +
                        qsubDistrict +
                        qserviceType +
                        qaccount +
                        qbudget +
                        qaccGroup +
                        qindustrialSec +
                        qgSizes +
                        qprovince +
                        qdistrict +
                        qcity +
                        qcostCenter +
                        " ORDER BY VPT.PRICING_RULE_ID ASC";
        Query query = em.createNativeQuery(queryString);
        if (sor != null) {
            query.setParameter("sor", sor);
        }
        if (custSegment != null) {
            query.setParameter("custSegment", custSegment);
        }
        if (accCat != null) {
            query.setParameter("accCat", accCat);
        }
        if (subDistrict != null) {
            query.setParameter("subDistrict", subDistrict);
        }
        if (serviceType != null) {
            query.setParameter("serviceType", serviceType);
        }
        if (account != null) {
            query.setParameter("account", account);
        }
        if (budget != null) {
            query.setParameter("budget", budget);
        }
        if (accGroup != null) {
            query.setParameter("accGroup", accGroup);
        }
        if (industrialSec != null) {
            query.setParameter("industrialSec", industrialSec);
        }
        if (gSizes != null) {
            query.setParameter("gSizes", gSizes);
        }
        if (province != null) {
            query.setParameter("province", province);
        }
        if (district != null) {
            query.setParameter("district", district);
        }
        if (city != null) {
            query.setParameter("city", city);
        }
        if (costCenter != null) {
            query.setParameter("costCenter", costCenter);
        }
        List<Object[]> resultList = query.getResultList();
        List<VW_PROMO_TIERING> infoList = new ArrayList<>();
        for (Object[] objects : resultList) {
            VW_PROMO_TIERING info = new VW_PROMO_TIERING();
            info.setTieringId(hasValue(objects[0]) ? Integer.parseInt(objects[0] + "") : null);
            info.setTiering(hasValue(objects[2]) ? objects[2]+"" : null);
            infoList.add(info);
        }
        return infoList;
    }

    public static boolean hasValue(Object o) {
        if (o == null || o.toString().trim().equals("") || o.toString().isEmpty()) {
            return false;
        }
        return true;

    }
}

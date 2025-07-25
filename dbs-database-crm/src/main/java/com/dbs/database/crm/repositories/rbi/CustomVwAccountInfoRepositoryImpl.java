/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.M_RBI_BILLING;
import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author RachmatY
 */
public class CustomVwAccountInfoRepositoryImpl implements CustomVwAccountInfoRepository{

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private MRbiBillingRepo mRbiBillingRepo;

    @Override
    public List<VW_ACCOUNT_INFORMATION> customMethod(Integer sorId,
                                                     List<Integer> costCenterId,
                                                     List<Integer> meterReadingCodeId,
                                                     List<Integer> accountSegmentId,
                                                     List<Integer> accountGroupTypeId,
                                                     Integer entityId) {
        String qMReadingCode = "";
        String qCostCenter = "";
        String qAccountSegment = "";
        String qAccountGroupType = "";
        if (!meterReadingCodeId.isEmpty() && meterReadingCodeId.size() > 0) {
            qMReadingCode = " AND METER_READING_CODE_ID in :meterReadingCodeId ";
        }

        if (!costCenterId.isEmpty() && costCenterId.size() > 0) {
            qCostCenter = " AND COST_CENTER_ID in :costCenterId ";
        }

        if (!accountSegmentId.isEmpty() && accountSegmentId.size() > 0) {
            qAccountSegment = " AND ACCOUNT_SEGMENT_ID in :accountSegmentId ";
        }
        if (!accountGroupTypeId.isEmpty() && accountGroupTypeId.size() > 0) {
            qAccountGroupType = " AND ACCOUNT_GROUP_TYPE_ID in :accountGroupTypeId ";
        }

        String queryString = "SELECT ACCOUNT_ID,ACCOUNT_NAME,ACCOUNT_NUMBER FROM VW_ACCOUNT_INFORMATION " +
                " WHERE SOR_ID = "+sorId+
                " AND ENTITY_ID = "+entityId+
                qCostCenter +
                qMReadingCode +
                qAccountSegment +
                qAccountGroupType;

        Query query = em.createNativeQuery(queryString);
        if (!costCenterId.isEmpty() && costCenterId.size() > 0) {
            query.setParameter("costCenterId", costCenterId);
        }
        if (!meterReadingCodeId.isEmpty() && meterReadingCodeId.size() > 0) {
            query.setParameter("meterReadingCodeId", meterReadingCodeId);
        }
        if (!accountSegmentId.isEmpty() && accountSegmentId.size() > 0) {
            query.setParameter("accountSegmentId", accountSegmentId);
        }
        if (!accountGroupTypeId.isEmpty() && accountGroupTypeId.size() > 0) {
            query.setParameter("accountGroupTypeId", accountGroupTypeId);
        }
        List<Object[]>  resultList = query.getResultList();
        List<VW_ACCOUNT_INFORMATION> infoList = new ArrayList<>();
        for (Object[] objects : resultList) {
            VW_ACCOUNT_INFORMATION info = new VW_ACCOUNT_INFORMATION();
            info.setAccountId(hasValue(objects[0]) ? Integer.parseInt(objects[0]+"") : null);
            info.setAccountName(hasValue(objects[1]) ? objects[1]+"" : null);
            Optional<M_RBI_BILLING> cekStatusApproval = mRbiBillingRepo.findByAccountNumberAndStatusApproval(objects.toString(), "APPROVED");
            if (cekStatusApproval.isEmpty()){
                info.setAccountNumber(hasValue(objects[2]) ? objects[2]+"" : null);
            } else {
                continue;
            }
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

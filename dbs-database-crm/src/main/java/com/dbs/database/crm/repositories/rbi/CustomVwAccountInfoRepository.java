/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import java.util.List;

/**
 *
 * @author RachmatY
 */
public interface CustomVwAccountInfoRepository {
    
    public List<VW_ACCOUNT_INFORMATION> customMethod(Integer sorId,
            List<Integer> costCenterId,
            List<Integer> meterReadingCodeId,
            List<Integer> accountSegmentId,
            List<Integer> accountGroupTypeId,
            Integer entityId);
    
}

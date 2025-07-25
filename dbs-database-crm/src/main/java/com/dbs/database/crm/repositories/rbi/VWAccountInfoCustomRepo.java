/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.database.crm.repositories.rbi;

import com.dbs.database.crm.entities.ratingbillinginvoice.view.VW_ACCOUNT_INFORMATION;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author RachmatY
 */
@Repository
@Transactional(value = "crmTransactionManager")
public interface VWAccountInfoCustomRepo extends JpaRepository<VW_ACCOUNT_INFORMATION, Integer>, CustomVwAccountInfoRepository{
    
}

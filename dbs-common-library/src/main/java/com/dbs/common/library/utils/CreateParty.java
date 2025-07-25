package com.dbs.common.library.utils;

import com.dbs.database.crm.entities.accountmanagement.M_PARTY;
import com.dbs.database.crm.repositories.accountmanagement.MPartyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
@SuppressWarnings("java:S6813")
public class CreateParty {

    @Autowired
    private MPartyRepo mPartyRepo;
    
    public static final String CUSTOMER = "CUSTOMER";
    public static final String ACCOUNT = "ACCOUNT";
    public static final String CONTACT = "CONTACT";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String PARTNER = "PARTNER";
    public static final String BANK = "BANK";

    public Integer createParty(String partyType, String partyObjectId,
            String partyUniqueValue, String partyName, Integer entityId) {
        M_PARTY mParty = new M_PARTY();
        mParty.setPartyType(partyType);
        mParty.setPartyObjectId(partyObjectId);
        mParty.setPartyUniqueValue(partyUniqueValue);
        mParty.setPartyName(partyName);
        mParty.setEntityId(entityId);
        mParty.setCreatedBy(UserDetailUtils.getUsername());
        mParty.setStatus(FlowStatus.ACTIVE.name());
        mParty.setCreatedDate(new Date());
        M_PARTY newParty = mPartyRepo.save(mParty);

        return newParty.getPartyId();
    }
    
    public Map<String, Object> updateParty(Integer partyId, String partyUniqueValue, String partyName) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Optional<M_PARTY> mPartyOptional = mPartyRepo.findByPartyId(partyId);
        if(mPartyOptional.isEmpty()) {
            return response;
        }
        M_PARTY mParty = mPartyOptional.get();
        mParty.setPartyUniqueValue(partyUniqueValue);
        mParty.setPartyName(partyName);
        mParty.setUpdatedBy(UserDetailUtils.getUsername());
        mParty.setUpdatedDate(new Date());
        M_PARTY updatedParty = mPartyRepo.save(mParty);


        response.put("partyId", updatedParty.getPartyId());
        response.put("partyType", updatedParty.getPartyType());
        response.put("partyObjectId", updatedParty.getPartyObjectId());
        response.put("partyUniqueValue", updatedParty.getPartyUniqueValue());
        response.put("partyName", updatedParty.getPartyName());
        response.put("entityId", updatedParty.getEntityId());
        response.put("updatedBy", updatedParty.getUpdatedBy());
        response.put("status", updatedParty.getStatus());
        response.put("updatedDate", updatedParty.getUpdatedDate());
        return response;

    }
    
    public Map<String, Object> activeInactiveParty(Integer partyId) {
        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
        Optional<M_PARTY> mPartyOptional = mPartyRepo.findByPartyId(partyId);
        if(mPartyOptional.isEmpty()) {
            return response;
        }
        M_PARTY mParty = mPartyOptional.get();
        M_PARTY updatedParty = new M_PARTY();
        if(mParty.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name())) {
            mParty.setStatus(FlowStatus.INACTIVE.name());
            mParty.setUpdatedBy(UserDetailUtils.getUsername());
            mParty.setUpdatedDate(new Date());
            updatedParty = mPartyRepo.save(mParty);
        } else if(mParty.getStatus().equalsIgnoreCase(FlowStatus.INACTIVE.name())){
            mParty.setStatus(FlowStatus.ACTIVE.name());
            mParty.setUpdatedBy(UserDetailUtils.getUsername());
            mParty.setUpdatedDate(new Date());
            updatedParty = mPartyRepo.save(mParty);
        }

        response.put("partyId", updatedParty.getPartyId());
        response.put("partyType", updatedParty.getPartyType());
        response.put("partyObjectId", updatedParty.getPartyObjectId());
        response.put("partyUniqueValue", updatedParty.getPartyUniqueValue());
        response.put("partyName", updatedParty.getPartyName());
        response.put("entityId", updatedParty.getEntityId());
        response.put("updatedBy", updatedParty.getUpdatedBy());
        response.put("status", updatedParty.getStatus());
        response.put("updatedDate", updatedParty.getUpdatedDate());
        return response;
    }

}

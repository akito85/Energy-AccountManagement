package com.dbs.database.crm.utils;


import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.entities.usermanagement.M_DATA_ACCESS_HIERARCHY;
import com.dbs.database.crm.entities.usermanagement.R_COSTCENTER_SIBLING;
import com.dbs.database.crm.entities.usermanagement.R_DATA_ACCESS_HIERARCHY;
import com.dbs.database.crm.repositories.usermanagement.MDataAccessHierarchyRepo;
import com.dbs.database.crm.repositories.usermanagement.MPositionRepo;
import com.dbs.database.crm.repositories.usermanagement.RDataAccessHierarchyRepo;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.dbs.common.base.utils.Constant.GET_CC_CHILD;
import static com.dbs.common.base.utils.Constant.GET_CC_PARENT;
import com.dbs.database.crm.repositories.usermanagement.MCostCenterRepo;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class CostCenterUtils {
    
    @Autowired
    private MPositionRepo mPositionRepoBase;
    @Autowired
    private RDataAccessHierarchyRepo rDataAccessHierarchyRepobase;
    @Autowired
    private MDataAccessHierarchyRepo mDataAccessHierarchyRepobase;

    private static MPositionRepo mPositionRepo;
    private static RDataAccessHierarchyRepo rDataAccessHierarchyRepo;
    private static MDataAccessHierarchyRepo mDataAccessHierarchyRepo;

    @PostConstruct
    @SuppressWarnings("java:S2696")
    public void init() {
        if (mPositionRepo == null) {
            mPositionRepo = mPositionRepoBase;
        }
        if (rDataAccessHierarchyRepo == null) {
            rDataAccessHierarchyRepo = rDataAccessHierarchyRepobase;
        }
        if (mDataAccessHierarchyRepo == null) {
            mDataAccessHierarchyRepo = mDataAccessHierarchyRepobase;
        }
    }
    

    public Integer findCostCenterByPositionId(Integer positionId){
        Optional<M_COSTCENTER> opt = mPositionRepo.findFirstByPositionIdAndStatusAndIsDeleted(positionId);
        if (opt.isPresent()) {
            M_COSTCENTER cc = opt.get();
            return cc.getCcId();
        }
        return null;
    }

    public List<Integer> findCostCenterByPositionId(Integer positionId, String selector){
        Optional<M_COSTCENTER> opt = mPositionRepo.findFirstByPositionIdAndStatusAndIsDeleted(positionId);
        List<Integer> ccLs = new ArrayList<>();
        if (opt.isPresent()) {
            Optional<M_DATA_ACCESS_HIERARCHY> dahOptional =  mDataAccessHierarchyRepo.findByStatus("ACTIVE");
            M_DATA_ACCESS_HIERARCHY dah = dahOptional.get();
            Integer activeDahId = dah.getDahId();
            M_COSTCENTER cc = opt.get();
            Integer currentCcId = cc.getCcId();
            ccLs.add(currentCcId); //NEW update - no parent id in M_COSCENTER table.
            if(selector == null ? GET_CC_CHILD == null : selector.equals(GET_CC_CHILD)){
                ccLs.addAll(findAllBottoms(currentCcId,activeDahId,ccLs));
            }
            else if(selector == null ? GET_CC_PARENT == null : selector.equals(GET_CC_PARENT)){
                ccLs.addAll(findTop(currentCcId,activeDahId));
            }
             //Get Sibling
            List<R_DATA_ACCESS_HIERARCHY> rdahList = rDataAccessHierarchyRepo.findAllCcIdActiveDah(currentCcId,activeDahId);
            Set<R_COSTCENTER_SIBLING> rcsList = new HashSet<>();
            for(R_DATA_ACCESS_HIERARCHY rdah : rdahList){
                Optional<M_DATA_ACCESS_HIERARCHY> parentDah = mDataAccessHierarchyRepo.findByDahIdAndStatus(rdah.getDahId(),"ACTIVE");
                if(parentDah.isPresent()){
                    rcsList.addAll(rdah.getRCostCenterSibling());
                }
            }
            for(R_COSTCENTER_SIBLING rcs : rcsList){
//                ccLs.addAll(findBottom(rcs.getSiblingId(), activeDahId));
                if(rcs.getStatus().equalsIgnoreCase("ACTIVE")) {
                    ccLs.addAll(findBottom(rcs.getSiblingId(), activeDahId));
                }
            }
            ccLs = ccLs.stream().distinct().collect(Collectors.toList());
            return ccLs;
        }
       return ccLs;
    }

    public List<Integer> findAllBottoms(Integer currentCcId, Integer activeDahId, List<Integer> ccLs) {
        List<Integer> child = findBottom(currentCcId, activeDahId);

        if (!child.isEmpty()) {
            ccLs.addAll(child);

            for (Integer a : child) {
                findAllBottoms(a, activeDahId, ccLs);
            }
        }
        return ccLs;
    }

    public List<Integer> findBottom(Integer currCCId, Integer activeDah){
        List<Integer> ccList = rDataAccessHierarchyRepo.findChild(currCCId,activeDah);
        List<Integer> child = new ArrayList<>();
//        child.add(currCCId);
        if(ccList != null){
            child.addAll(ccList);
        }
        return child;
    }

    public List<Integer> findTop(Integer ccId, Integer activeDah){
        List<Integer> ccList = rDataAccessHierarchyRepo.findCcId(ccId,activeDah);
        List<Integer> res = new ArrayList<>();
        List<Integer> parent = getParentLoop(ccList,res,ccId,activeDah).stream().distinct().collect(Collectors.toList());
        return parent;
    }
    
    public List<Integer> getParentLoop(List<Integer> ccLs, List<Integer> res, Integer initial, Integer activeDah){
        for(Integer cc : new ArrayList<Integer>(ccLs)){
            res.add(cc);
            List<Integer> newData = rDataAccessHierarchyRepo.findCcId(cc,activeDah).stream().filter(b-> !ccLs.contains(b) && !b.equals(initial)).collect(Collectors.toList());
            if(!newData.isEmpty() && !ccLs.containsAll(newData)){
                for(Integer a : newData){
                    if(!ccLs.contains(a)){
                        ccLs.add(a);
                        res.addAll(getParentLoop(ccLs,res,initial,activeDah));
                    }
                }
            }
        }
        return res;
    }
}

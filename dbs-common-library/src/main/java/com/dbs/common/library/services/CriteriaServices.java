/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.utils.FlowStatus;
import com.dbs.common.library.utils.dto.DropDownCriteriaDto;
import com.dbs.database.crm.entities.product.M_LOCATION;
import com.dbs.database.crm.entities.product.M_PRODUCT;
import com.dbs.database.crm.entities.usermanagement.M_COSTCENTER;
import com.dbs.database.crm.repositories.mastermanagement.MLocationsRepo;
import com.dbs.database.crm.repositories.product.MProductRepo;
import com.dbs.database.crm.repositories.usermanagement.MCostCenterRepo;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author RachmatY
 */
@Service
public class CriteriaServices {
    
    @Autowired
    private GlobalTypeValueService globalTypeValueService;
    
    @Autowired
    private MLocationsRepo mLocationRepo;
    
    @Autowired
    private MProductRepo mProductRepo;
    
    @Autowired
    private MCostCenterRepo mCostCenterRepo;
    
    
    public List<LinkedHashMap<String, Object>> getCriteriaByGlobalType(String name){
        return globalTypeValueService.getGlobalTypeByGroupName(name);
    }
    
    public List<DropDownCriteriaDto> getCriteriaProvince(){
        List<M_LOCATION> locationList = mLocationRepo.findAllByLocationTypeAndLocationParent(Constant.LOCATION_TYPE_PROVINCE, 1);
        List<DropDownCriteriaDto> dtos = new ArrayList<>();
        if (!locationList.isEmpty()) {
            for (M_LOCATION location : locationList) {
               DropDownCriteriaDto dto = new DropDownCriteriaDto();
               dto.setId(location.getLocationId());
               dto.setCode(location.getLocationCode());
               dto.setText(location.getLocationName());
               dtos.add(dto);
            }
        }
        return dtos;
    }
    
    public List<DropDownCriteriaDto> getCriteriaLocationByParent(String name, Integer parentId){
        List<M_LOCATION> locationList = mLocationRepo.findAllByLocationTypeAndLocationParent(name, parentId);
        List<DropDownCriteriaDto> dtos = new ArrayList<>();
        if (!locationList.isEmpty()) {
            for (M_LOCATION location : locationList) {
               DropDownCriteriaDto dto = new DropDownCriteriaDto();
               dto.setId(location.getLocationId());
               dto.setCode(location.getLocationCode());
               dto.setText(location.getLocationName());
               dtos.add(dto);
            }
        }
        return dtos;
    }
    
    public List<DropDownCriteriaDto> getCriteriaProduct(){
        Optional<List<M_PRODUCT>> productOpt = mProductRepo.findAllByStatus(FlowStatus.ACTIVE.name());
        List<DropDownCriteriaDto> dtos = new ArrayList<>();
        if (productOpt.isPresent()) {
            for (M_PRODUCT domain : productOpt.get()) {
               DropDownCriteriaDto dto = new DropDownCriteriaDto();
               dto.setId(domain.getId());
               dto.setCode(domain.getProductName());
               dto.setText(domain.getProductDescription());
               dtos.add(dto);
            }
        }
        return dtos;
    }
    
    public List<DropDownCriteriaDto> getCriteriaAreaAndSor(String name){
        Optional<List<M_COSTCENTER>> domainOpt = mCostCenterRepo.findAllByCcTypeIgnoreCaseAndIsDeleted(name, false);
        List<DropDownCriteriaDto> dtos = new ArrayList<>();
        if (domainOpt.isPresent()) {
            for (M_COSTCENTER domain : domainOpt.get()) {
               DropDownCriteriaDto dto = new DropDownCriteriaDto();
               dto.setId(domain.getCcId());
               dto.setCode(domain.getCode());
               dto.setText(domain.getCode()+" - "+domain.getName());
               dtos.add(dto);
            }
        }
        return dtos;
    }
    
    public List<DropDownCriteriaDto> getCriteriaLocation(String type, String locationType, Integer locationParent){
        List<M_LOCATION> locationList;
        if (Constant.LOCATION_TYPE_COUNTRY.equalsIgnoreCase(type)) {
            locationList = mLocationRepo.findAllByLocationType(Constant.LOCATION_TYPE_COUNTRY);
        } else {
            locationList = mLocationRepo.findAllByLocationTypeAndLocationParent(locationType, locationParent);
        }
        
        List<DropDownCriteriaDto> dtos = new ArrayList<>();
        if (!locationList.isEmpty()) {
            for (M_LOCATION location : locationList) {
               DropDownCriteriaDto dto = new DropDownCriteriaDto();
               dto.setId(location.getLocationId());
               dto.setCode(location.getLocationCode());
               dto.setText(location.getLocationName());
               dtos.add(dto);
            }
        }
        return dtos;
    }
}

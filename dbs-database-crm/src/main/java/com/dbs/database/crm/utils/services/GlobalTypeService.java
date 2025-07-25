package com.dbs.database.crm.utils.services;

import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GlobalTypeService {
    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    public List<R_GLOBAL_TYPE_VALUE> listGlobalTypeValueById(Integer id) {
        return rGlobalTypeValueRepo.findByGlobalType(id).stream().filter(glb -> !glb.getIsDeleted()).collect(Collectors.toList());
    }

    public Optional<R_GLOBAL_TYPE_VALUE> getGlobalTypeValueById(Integer id) {
        return Optional.ofNullable(rGlobalTypeValueRepo.findTopByGlbTypeValIdAndIsDeleted(id, false));
    }
}

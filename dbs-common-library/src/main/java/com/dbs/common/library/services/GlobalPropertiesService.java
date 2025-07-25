package com.dbs.common.library.services;

import com.dbs.common.library.utils.FlowStatus;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_PROPERTIES;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import com.dbs.database.crm.repositories.usermanagement.MGlobalPropertiesRepo;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings("java:S6813")
public class GlobalPropertiesService {
    
    @Autowired
    private MGlobalPropertiesRepo mGlobalPropertiesRepo;
    
    public R_GLOBAL_PROPERTIES_DTL getGlobalProperties(String groupName, String value) {
        Optional<M_GLOBAL_PROPERTIES> mGlobalProperties = mGlobalPropertiesRepo.findByName(groupName);
        if (mGlobalProperties.isPresent()) {
            Optional<R_GLOBAL_PROPERTIES_DTL> rgpOptional = mGlobalProperties.get().getRGlobalPropertiesDtls().stream()
                            .filter(e -> e.getGpdKey().equalsIgnoreCase(value))
                            .findFirst();
            return rgpOptional.orElse(null);
        }
        return null;
    }
    
    public List<R_GLOBAL_PROPERTIES_DTL> getGlobalPropertieses(String groupName) {
        Optional<M_GLOBAL_PROPERTIES> mGlobalProperties = mGlobalPropertiesRepo.findByName(groupName);
        if (mGlobalProperties.isPresent()) {
            List<R_GLOBAL_PROPERTIES_DTL> rgpOptional = mGlobalProperties.get().getRGlobalPropertiesDtls().stream()
                            .filter(e -> e.getStatus().equalsIgnoreCase(FlowStatus.ACTIVE.name()))
                            .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(rgpOptional)) {
                return rgpOptional;
            }
            return Collections.emptyList();
        }
        return Collections.emptyList();
    }
}

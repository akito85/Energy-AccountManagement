package com.dbs.common.library.services;

import com.dbs.common.library.utils.FlowStatus;
import com.dbs.database.crm.entities.usermanagement.M_GLOBAL_TYPE;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_TYPE_VALUE;
import com.dbs.database.crm.repositories.usermanagement.MGlobalTypeRepo;
import com.dbs.database.crm.repositories.usermanagement.RGlobalTypeValueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@SuppressWarnings("java:S6813")
public class GlobalTypeValueService {
    
    @Autowired
    private MGlobalTypeRepo mGlobalTypeRepo;

    @Autowired
    private RGlobalTypeValueRepo rGlobalTypeValueRepo;

    private static final String OTHER = "OTHER";
    
    public R_GLOBAL_TYPE_VALUE getGlobalTypeByGlbValue(String groupName, String value) {
        Optional<R_GLOBAL_TYPE_VALUE> rGOptional = Optional.empty();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rGOptional = gloOptional.get().getRGlobalTypeValues().stream().filter(e -> e.getGlbValue().equals(value)).findFirst();
        }
        return rGOptional.orElse(null);
    }
    
    public R_GLOBAL_TYPE_VALUE getGlobalTypeByGlbTypeValId(String groupName, Integer idDetail) {
        Optional<R_GLOBAL_TYPE_VALUE> rGOptional = Optional.empty();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rGOptional = gloOptional.get().getRGlobalTypeValues().stream().filter(e -> e.getGlbTypeValId().equals(idDetail)).findFirst();
        }
        return rGOptional.orElse(null);
    }
    
    public Optional<R_GLOBAL_TYPE_VALUE> getOptionalGlobalTypeByGlbTypeValId(String groupName, Integer idDetail) {
        Optional<R_GLOBAL_TYPE_VALUE> rGOptional = Optional.empty();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rGOptional = gloOptional.get().getRGlobalTypeValues().stream().filter(e -> e.getGlbTypeValId().equals(idDetail)).findFirst();
        }
        return rGOptional;
    }
    
    public List<R_GLOBAL_TYPE_VALUE> getGlobalTypeByParentValue(String groupName, Integer parentValue) {
        List<R_GLOBAL_TYPE_VALUE> rGs = new ArrayList<>();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rGs = gloOptional.get().getRGlobalTypeValues()
                    .stream().filter(e -> e.getParentValue().equals(parentValue)).collect(Collectors.toList());
        }
        return rGs;
    }
    
    public Optional<R_GLOBAL_TYPE_VALUE> getOptionalGlobalTypeByGlbValue(String groupName, String glbValue) {
        Optional<R_GLOBAL_TYPE_VALUE> rG = Optional.empty();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rG = gloOptional.get().getRGlobalTypeValues()
                    .stream().filter(e -> e.getGlbValue().equals(glbValue)).findFirst();
        }
        return rG;
    }
    
    public List<R_GLOBAL_TYPE_VALUE> getDetailGlobalType(String groupName) {
        List<R_GLOBAL_TYPE_VALUE> rGs = new ArrayList<>();
        Optional<M_GLOBAL_TYPE> gloOptional = mGlobalTypeRepo.findFirstByGroupNameAndStatus(groupName, FlowStatus.ACTIVE.name());
        if (gloOptional.isPresent()) {
            rGs = gloOptional.get().getRGlobalTypeValues()
                    .stream().collect(Collectors.toList());
        }
        return rGs;
    }
    
    @SuppressWarnings("java:S1172")
    public List<LinkedHashMap<String, Object>> getGlobalType(String groupName, HttpServletRequest httpServletRequest) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty()) {
            return Collections.emptyList();
        }
        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();
        
        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("name", v.getName());
                    response.put("value", v.getGlbValue());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    private Stream<R_GLOBAL_TYPE_VALUE> sortBy(M_GLOBAL_TYPE globalType, String sortBy){
        switch (sortBy) {
            case "CREATED_DATE_ASC":
                return globalType.getRGlobalTypeValues().stream()
                        .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                        .sorted(Comparator.comparing(R_GLOBAL_TYPE_VALUE::getCreatedDate));
            case "CREATED_DATE_DSC":
                return globalType.getRGlobalTypeValues().stream()
                        .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                        .sorted(Comparator.comparing(R_GLOBAL_TYPE_VALUE::getCreatedDate).reversed());
            case "ALPHABET_ASC":
                return globalType.getRGlobalTypeValues().stream()
                        .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                        .sorted(Comparator.comparing(R_GLOBAL_TYPE_VALUE::getName));
            case "ALPHABET_DSC":
                return globalType.getRGlobalTypeValues().stream()
                        .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                        .sorted(Comparator.comparing(R_GLOBAL_TYPE_VALUE::getName).reversed());
            default:
                return globalType.getRGlobalTypeValues().stream()
                        .filter(v -> FlowStatus.ACTIVE.name().equalsIgnoreCase(v.getStatus()))
                        .sorted(Comparator.comparing(R_GLOBAL_TYPE_VALUE::getGlbOrder));
        }
    }
    
    @SuppressWarnings("java:S1172")
    public List<LinkedHashMap<String, Object>> getGlobalTypeOther(String groupName, HttpServletRequest httpServletRequest) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty()) {
            return Collections.emptyList();
        }
        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();
        
        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("name", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeOtherWithValue(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty()) {
            return Collections.emptyList();
        }

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("name", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeCriteria(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty())
            return Collections.emptyList();

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("Id", v.getGlbTypeValId());
                    response.put("code", v.getGlbValue());
                    response.put("text", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeOtherByParentValue(String groupName, Integer parentValue) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty())
            return Collections.emptyList();

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }

        return globalTypeValues
                .filter(v -> v.getParentValue().equals(parentValue))
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("Id", v.getGlbTypeValId());
                    response.put("code", v.getGlbValue());
                    response.put("text", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeProduct(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty())
            return Collections.emptyList();

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("code", v.getGlbValue());
                    response.put("text", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeProductByParent(String groupName, Integer id) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty())
            return Collections.emptyList();

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .filter(w -> w.getParentValue().equals(id))
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("code", v.getGlbValue());
                    response.put("text", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }
    
    public List<LinkedHashMap<String, Object>> getGlobalTypeInvoice(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty()) {
            return Collections.emptyList();
        }
        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();
        
        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("name", v.getName());
                    response.put("glbTypeValId", v.getGlbTypeValId());
                    return response;
                })
                .collect(Collectors.toList());
    }

    @SuppressWarnings("java:S4144")
    public List<LinkedHashMap<String, Object>> getGlobalTypeByGroupName(String groupName) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty())
            return Collections.emptyList();

        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues;
        if (optRgtv.isPresent()) {
            globalTypeValues = sortBy(globalType, globalType.getSortBy());
        } else {
            globalTypeValues = sortBy(globalType, OTHER);
        }
        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("code", v.getGlbValue());
                    response.put("text", v.getName());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LinkedHashMap<String, Object>> getGlobalTypeAnyChild(String groupName, Integer parentValue) {
        Optional<M_GLOBAL_TYPE> mGlobalType = mGlobalTypeRepo.findByGroupName(groupName);
        if (mGlobalType.isEmpty()) {
            return Collections.emptyList();
        }
        M_GLOBAL_TYPE globalType = mGlobalType.get();
        // check in order null or empty
        Optional<R_GLOBAL_TYPE_VALUE> optRgtv = globalType.getRGlobalTypeValues().stream()
                .filter(e -> FlowStatus.ACTIVE.name().equalsIgnoreCase(e.getStatus()))
                .filter(v -> Objects.isNull(v.getGlbOrder()))
                .findAny();

        Stream<R_GLOBAL_TYPE_VALUE> globalTypeValues = optRgtv.isPresent() ? sortBy(globalType, globalType.getSortBy()) : sortBy(globalType, OTHER);

        if(parentValue!=null) {
            globalTypeValues = globalTypeValues.filter(v -> v.getParentValue().equals(parentValue));
        }

        return globalTypeValues
                .map(v -> {
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("id", v.getGlbTypeValId());
                    response.put("name", v.getName());
                    response.put("isAnyChild", rGlobalTypeValueRepo.existsByParentValue(v.getGlbTypeValId())? Boolean.TRUE : Boolean.FALSE);
                    return response;
                })
                .collect(Collectors.toList());
    }

}

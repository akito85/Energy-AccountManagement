package com.dbs.common.library.utils;

import com.dbs.common.library.entities.CriteriaData;

import java.util.*;
import java.util.stream.Collectors;

import com.dbs.database.crm.entities.accountmanagement.VW_ACCOUNT_CRITERIA;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckCriteria {

    private CheckCriteria() {}

    private static final Logger logger = LoggerFactory.getLogger(CheckCriteria.class);

    public static final String PRODUCT = "product";
    public static final String TERM_OF_PAYMENT = "termOfPayment";
    public static final String INVOICE_TEMPLATE = "invoiceTemplate";

    @SuppressWarnings({"java:S3776", "java:S6541", "java:S135"})
    public static Integer checkCondition(Optional<VW_ACCOUNT_CRITERIA> dataAccount, List<CriteriaData> masterCriteriaDTO) {

        Integer valueId;
        Boolean cekFlag = true;
        List<CriteriaData> keStep2 = new ArrayList<>();
        Integer idIsALlY = 0;

        for (CriteriaData c : masterCriteriaDTO) {

            // SAVE id isAll IS TRUE
            if (Boolean.TRUE==c.getIsAll()) {
                idIsALlY = c.getId();
                continue;
            }

            // CHECK CRITERIA
            if (c.getAccountNumber() == null) {
                cekFlag = true;
            } else if (!c.getAccountNumber().equalsIgnoreCase(dataAccount.get().getAccountNumber())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountBudget() == null) {
                cekFlag = true;
            } else if (!c.getAccountBudget().equals(dataAccount.get().getAccountBudget())) {
                cekFlag = false;
                continue;
            }
            if (c.getGsizes() == null) {
                cekFlag = true;
            } else if (!c.getGsizes().equals(dataAccount.get().getPremiseSpAssetGSize())) {
                cekFlag = false;
                continue;
            }
            if (c.getPremiseSubdistrict() == null) {
                cekFlag = true;
            } else if (!c.getPremiseSubdistrict().equals(dataAccount.get().getPremiseSubdistrict())) {
                cekFlag = false;
                continue;
            }
            if (c.getPremiseDistrict() == null) {
                cekFlag = true;
            } else if (!c.getPremiseDistrict().equals(dataAccount.get().getPremiseDistrict())) {
                cekFlag = false;
                continue;
            }
            if (c.getPremiseCity() == null) {
                cekFlag = true;
            } else if (!c.getPremiseCity().equals(dataAccount.get().getPremiseCity())) {
                cekFlag = false;
                continue;
            }
            if (c.getPremiseProvince() == null) {
                cekFlag = true;
            } else if (!c.getPremiseProvince().equals(dataAccount.get().getPremiseProvince())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountClassificationType() == null) {
                cekFlag = true;
            } else if (!c.getAccountClassificationType().equals(dataAccount.get().getAccountClassificationType())) {
                cekFlag = false;
                continue;
            }
            if (c.getCostCenter() == null) {
                cekFlag = true;
            } else if (!c.getCostCenter().equals(dataAccount.get().getCostCenter())) {
                cekFlag = false;
                continue;
            }
            if (c.getSor() == null) {
                cekFlag = true;
            } else if (!c.getSor().equals(dataAccount.get().getSor())) {
                cekFlag = false;
                continue;
            }
            if (c.getWapuFlag() == null) {
                cekFlag = true;
            } else if (!c.getWapuFlag().equals(dataAccount.get().getAccountWapuFlag())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountIndustrialSector() == null) {
                cekFlag = true;
            } else if (!c.getAccountIndustrialSector().equals(dataAccount.get().getAccountIndustrialSector())) {
                cekFlag = false;
                continue;
            }
            if (c.getCorporateFlag() == null) {
                cekFlag = true;
            } else if (!c.getCorporateFlag()==dataAccount.get().getAccountCorporateFlag()) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountSegment() == null) {
                cekFlag = true;
            } else if (!c.getAccountSegment().equals(dataAccount.get().getAccountSegment())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountGroupType() == null) {
                cekFlag = true;
            } else if (!c.getAccountGroupType().equals(dataAccount.get().getAccountGroupType())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountCategory() == null) {
                cekFlag = true;
            } else if (!c.getAccountCategory().equals(dataAccount.get().getAccountCategory())) {
                cekFlag = false;
                continue;
            }

            if (c.getAccountType() == null) {
                cekFlag = true;
            } else if (!c.getAccountType().equals(dataAccount.get().getAccountAccountType())) {
                cekFlag = false;
                continue;
            }
            if (c.getPremiseCountry() == null) {
                cekFlag = true;
            } else if (!c.getPremiseCountry().equals(dataAccount.get().getPremiseCountry())) {
                cekFlag = false;
                continue;
            }
            if (c.getSaType() == null) {
                cekFlag = true;
            } else if (!c.getSaType().equals(dataAccount.get().getSaType())) {
                cekFlag = false;
                continue;
            }
            if (c.getSaServiceType() == null) {
                cekFlag = true;
            } else if (!c.getSaServiceType().equals(dataAccount.get().getSaServiceType())) {
                cekFlag = false;
                continue;
            }
            if (c.getSaProductVersion() == null) {
                cekFlag = true;
            } else if (!c.getSaProductVersion().equals(dataAccount.get().getSaProductVersion())) {
                cekFlag = false;
                continue;
            }
            if (c.getAccountClassificationType() == null) {
                cekFlag = true;
            } else if (!c.getAccountClassificationType().equals(dataAccount.get().getAccountClassificationType())) {
                cekFlag = false;
                continue;
            }

            // ADD DATA IF CRITERIA IS MATCHES
            if (cekFlag.equals(true)) {
                keStep2.add(c);
            }
        }

        // RETURN ID CRITERIA
        if (keStep2.isEmpty()) {
            // IF NO DATA CRITERIA MATCHES
            valueId = idIsALlY;
        } else {

            // GET PRIORITY BASED ON ALL DATA CRITERIA MATCHES
            List<Integer> idPriority = new ArrayList<>();
            int getIndexId = 0;
            List<Integer> saveIdIndex = new ArrayList<>();
            for (CriteriaData cc : keStep2) {
                getIndexId++;
                if (cc.getAccountNumber() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getAccountBudget() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getGsizes() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getPremiseSubdistrict() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getPremiseDistrict() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getPremiseCity() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getPremiseProvince() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getMeterReadingCode() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getCostCenter() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getSor() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getWapuFlag() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getAccountIndustrialSector() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getCorporateFlag() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getAccountSegment() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getAccountGroupType() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getAccountCategory() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                //tambahan
                getIndexId++;
                if (cc.getSaType() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                    continue;
                }
                getIndexId++;
                if (cc.getSaServiceType() != null) {
                    idPriority.add(cc.getId());
                    saveIdIndex.add(getIndexId);
                    getIndexId = 0;
                }
            }

            logger.info("checkCriteria : {}", saveIdIndex);

            if(saveIdIndex.isEmpty()) {
                valueId = idIsALlY;
            } else {
                // GET ID PRIORITY INDEX
                int min = saveIdIndex.get(0);
                int minIndex = 0;
                for (int i = 1; i < saveIdIndex.size(); i++) {
                    if (saveIdIndex.get(i) < min) {
                        min = saveIdIndex.get(i);
                        minIndex = i;
                    }
                }

                valueId = idPriority.get(minIndex);
            }

        }

        return valueId;
    }

    // for compare data product
    public static boolean compareCriteria(String dataName, Map<String, Integer> tasCrit,
                                          Map<String, Integer> destCrit) {

        LinkedHashMap<String, Integer> orderedCrit;
        switch (dataName) {
            case PRODUCT :
                orderedCrit = getDataProduct();
                break;
            case TERM_OF_PAYMENT:
                orderedCrit = getDataTermOfPayment();
                break;
            case INVOICE_TEMPLATE:
                orderedCrit = getInvoiceTemplate();
                break;
            default:
                orderedCrit = null;
        }

        //Check number of criterias
        //remove null criterias
        tasCrit = tasCrit.entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));
        destCrit = destCrit.entrySet().stream().filter(e -> e.getValue() != null).collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()));

        if (tasCrit.size() < destCrit.size()) {
            return false;
        }

        boolean matched = false;
        LinkedHashMap<String, Integer> tasCritOrderd = orderedCrit;
        LinkedHashMap<String, Integer> desCritOrderd = orderedCrit;

        //map to ordered criteria by priority
        for (Map.Entry<String, Integer> p : tasCrit.entrySet()) {
            tasCritOrderd.put(p.getKey(), p.getValue());
        }

        for (Map.Entry<String, Integer> p : destCrit.entrySet()) {
            desCritOrderd.put(p.getKey(), p.getValue());
        }

        //compare between criteria
        for (Map.Entry<String, Integer> p : desCritOrderd.entrySet()) {

            if (p.getValue() != null && tasCrit.get(p.getKey()) != null) {
                matched = p.getValue().equals(tasCrit.get(p.getKey()));

                if (!matched) {
                    break;
                }
            } else if (p.getValue() != null && tasCrit.get(p.getKey()) == null) {
                matched = false;
                break;
            }
        }

        return matched;
    }

    // for get data product criteria data
    public static LinkedHashMap<String, Integer> getDataProduct() {
        LinkedHashMap<String, Integer> orderedCrit = new LinkedHashMap<>();
        orderedCrit.put("customer", null);
        orderedCrit.put("budget", null);
        orderedCrit.put("subDistrict", null);
        orderedCrit.put("district", null);
        orderedCrit.put("city", null);
        orderedCrit.put("province", null);
        orderedCrit.put("costCenter", null);
        orderedCrit.put("sor", null);
        orderedCrit.put("industrialSector", null);
        orderedCrit.put("customerSegment", null);
        orderedCrit.put("accountClass", null);
        orderedCrit.put("accountGroup", null);
        orderedCrit.put("accountCategory", null);
        orderedCrit.put("serviceType", null);
        return orderedCrit;
    }

    // for get data term of payment criteria data
    public static LinkedHashMap<String, Integer> getDataTermOfPayment() {
        LinkedHashMap<String, Integer> orderedCrit = new LinkedHashMap<>();
        orderedCrit.put("customer", null);
        orderedCrit.put("budget", null);
        orderedCrit.put("subDistrict", null);
        orderedCrit.put("district", null);
        orderedCrit.put("city", null);
        orderedCrit.put("province", null);
        orderedCrit.put("sor", null);
        orderedCrit.put("area", null);
        orderedCrit.put("industrialSector", null);
        orderedCrit.put("serviceType", null);
        orderedCrit.put("product", null);
        orderedCrit.put("gsizes", null);
        orderedCrit.put("customerSegment", null);
        orderedCrit.put("accountGroupType", null);
        orderedCrit.put("accountCategory", null);
        return orderedCrit;
    }

    // for get data invoice template criteria data
    public static LinkedHashMap<String, Integer> getInvoiceTemplate() {
        LinkedHashMap<String, Integer> orderedCrit = new LinkedHashMap<>();
        orderedCrit.put("customer", null);
        orderedCrit.put("budget", null);
        orderedCrit.put("subDistrict", null);
        orderedCrit.put("district", null);
        orderedCrit.put("city", null);
        orderedCrit.put("province", null);
        orderedCrit.put("sor", null);
        orderedCrit.put("area", null);
        orderedCrit.put("industrialSector", null);
        orderedCrit.put("serviceType", null);
        orderedCrit.put("product", null);
        orderedCrit.put("gSizes", null);
        orderedCrit.put("customerSegment", null);
        orderedCrit.put("accountGroup", null);
        orderedCrit.put("accountCategory", null);
        return orderedCrit;
    }

}


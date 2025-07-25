package com.dbs.database.crm.repositories.product.promo;

import com.dbs.database.crm.entities.product.promo.VW_PROMO_TIERING;

import java.util.List;

public interface CustomCriteriaTieringRepository {
    public List<VW_PROMO_TIERING> customTiering(Integer sor,
                                                Integer custSegment,
                                                Integer accCat,
                                                Integer subDistrict,
                                                Integer serviceType,
                                                Integer account,
                                                Integer budget,
                                                Integer accGroup,
                                                Integer industrialSec,
                                                Integer gSizes,
                                                Integer province,
                                                Integer district,
                                                Integer city,
                                                Integer costCenter);
}

package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_ADDRESS;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ChooseAddressInterface {
    Page<VW_CHOOSE_ADDRESS> findByAddressIdIn(String type, @Nullable Specification<VW_CHOOSE_ADDRESS> specification, MaterialTablePagingRequest pagingData, Pageable pageable, List<Integer> addressIds, Class<VW_CHOOSE_ADDRESS> entityClass);
}

package com.dbs.database.crm.repositories.accountmanagement.Account;

import com.dbs.common.base.utils.MaterialTablePagingRequest;
import com.dbs.database.crm.entities.accountmanagement.VW_CHOOSE_CONTACT;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ChooseContactInterface {
    Page<VW_CHOOSE_CONTACT> findByContactIdIn(String type, @Nullable Specification<VW_CHOOSE_CONTACT> specification, MaterialTablePagingRequest pagingData, Pageable pageable, List<Integer> contactIds, Class<VW_CHOOSE_CONTACT> entityClass);
}

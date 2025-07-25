package com.dbs.database.crm.repositories.usermanagement;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import com.dbs.database.crm.entities.usermanagement.LOG_USER_LOGIN;

@Repository
@Transactional(value = "crmTransactionManager")
public interface LogUserLoginRepo extends PagingAndSortingRepository <LOG_USER_LOGIN, Integer>, JpaSpecificationExecutor<LOG_USER_LOGIN> {


    void deleteByUsernameAndSessionId(Integer username, String sessionId);
    
    Optional<LOG_USER_LOGIN>findByUsername(String username);
    
    List<LOG_USER_LOGIN>findAllByUsernameAndIsActive(String username, boolean isActive);
    
    List<LOG_USER_LOGIN>findAllByUsernameAndIsActiveAndIsLogin(String username, boolean isActive, boolean isLogin);
    
    Optional<LOG_USER_LOGIN>findByTokenAndSessionId(String token, String sessionId);
    
    Optional<LOG_USER_LOGIN>findBySessionId(String sessionId);
    
    Optional<LOG_USER_LOGIN>findByToken(String token);
    
    Optional<LOG_USER_LOGIN>findByTokenAndUsernameAndIsActiveAndIsLogin(String token, String username, boolean isActive, boolean isLogin);
    
}

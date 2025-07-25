/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dbs.common.library.interceptor;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.config.jwt.JwtUtils;
import com.dbs.common.library.ctrl.ResponseObject;
import com.dbs.database.crm.entities.usermanagement.LOG_SYS_SERVICES;
import com.dbs.database.crm.entities.usermanagement.M_MAINTENANCE_MODE;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.repositories.usermanagement.LogSysServicesRepo;
import com.dbs.database.crm.repositories.usermanagement.MMaintenanceModeRepo;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author RachmatY
 */
@Component
public class Interceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(Interceptor.class);
    @Autowired
    private LogSysServicesRepo logSysServicesRepo;
    @Autowired
    private MMaintenanceModeRepo mMaintenanceModeRepo;
    @Autowired
    private MUserRepo auRepo;
    
    @Autowired
    private JwtUtils jwtUtils;
    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        String token = jwtUtils.parseJwt(request);
        String agent = request.getHeader("User-Agent");
        String origin = request.getHeader("Origin");
        long startTime = (Long) request.getAttribute("startTime");
        long finishTime = System.currentTimeMillis();
        long executeTime = finishTime - startTime;
        
        LOG_SYS_SERVICES logSysServices = new LOG_SYS_SERVICES();
        String username = null;
        if (!StringUtils.isAnyBlank(token)) {
            username = jwtUtils.getUserNameFromJwtToken(token);
        }
        logSysServices.setVusername(!StringUtils.isAnyBlank(username) ? username : "ANONYMOUS");
        logSysServices.setVagent(agent);
        logSysServices.setVorigin(origin);
        logSysServices.setVurl(request.getRequestURL().toString());
        logSysServices.setVipaddress(request.getRemoteAddr());
        logSysServices.setIexectime(Math.toIntExact(executeTime));
        logSysServices.setVmethod(request.getMethod());
        logSysServicesRepo.save(logSysServices);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);
        boolean isExcludeFilter = shouldNotFilter(request);
        boolean isSuperUser = isAuthenticateUser(request);
        List<M_MAINTENANCE_MODE> mms = mMaintenanceModeRepo.findByStatus("ACTIVE");
//        System.out.println("Filter => "+isExcludeFilter);
//        System.out.println("Username => "+isSuperUser);
        if (!mms.isEmpty() && !isExcludeFilter) {
            if (!isSuperUser) {
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, HttpStatus.SERVICE_UNAVAILABLE,
                    ResponseUtils.SERVICE_UNAVAILABLE, "Service Unavailable because under maintenance");
                String jsonResponse = objectMapper.writeValueAsString(error);
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
                response.getOutputStream().print(jsonResponse);
                return false; // Stop further processing
            }
        }
        return true;
    }

    public LogSysServicesRepo getLogSysServicesRepo() {
        return logSysServicesRepo;
    }

    public void setLogSysServicesRepo(LogSysServicesRepo logSysServicesRepo) {
        this.logSysServicesRepo = logSysServicesRepo;
    }

    public JwtUtils getJwtUtils() {
        return jwtUtils;
    }

    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
    
    private final Set<String> excludedPaths = Set.of(
            "/v1/dbs/api/auth/login-su",
            "/v1/dbs/api/auth/login-background",
            "/v1/dbs/api/file/download2/"
    );
    
    private boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(path::startsWith);
    }
    
    public boolean isAuthenticateUser(HttpServletRequest httpServletRequest) {
        try {
            String tokenFromHeader = jwtUtils.parseJwt(httpServletRequest);
            if (!StringUtils.isEmpty(tokenFromHeader)) {
                String username = jwtUtils.getUserNameFromJwtToken(tokenFromHeader);
                Optional<M_USER> user = auRepo.findByUsername(username);
                if (user.isPresent() && Constant.SUPER_USER.equalsIgnoreCase(user.get().getUserLevel())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }
    
}

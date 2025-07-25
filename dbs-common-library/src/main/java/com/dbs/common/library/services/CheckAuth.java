package com.dbs.common.library.services;

import com.dbs.common.base.utils.Constant;
import com.dbs.common.library.config.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
@SuppressWarnings("java:S6813")
public class CheckAuth {
    
    @Autowired
    private JwtUtils jwtUtils;

    public String authenticateUser(HttpServletRequest httpServletRequest) {
        try {
            String tokenFromHeader = jwtUtils.parseJwt(httpServletRequest);
            return jwtUtils.getUserNameFromJwtToken(tokenFromHeader);
        } catch (Exception e) {
            return Constant.ERROR;
        }
    }
}

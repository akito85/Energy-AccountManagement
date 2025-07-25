
package com.dbs.common.library.utils;

import com.dbs.common.library.config.jwt.JwtUtils;
import com.dbs.common.library.impl.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@SuppressWarnings({"java:S6813", "java:S2696"})
public class UserDetailUtils {
    @Autowired
    private JwtUtils jwtUtilsBase;

    private static JwtUtils jwtUtils;

    private UserDetailUtils() {

    }

    @PostConstruct
    public void init() {
        if (jwtUtils == null) {
            jwtUtils = jwtUtilsBase;
        }
    }

    public static Authentication getAuthentication() {
        try {
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (Exception e) {
            return null;
        }
    }

    public static UserDetails getUserPrincipal() {
        try {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            return (UserDetails) userDetails.getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUserId() {
        try {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDt = (UserDetailsImpl) userDetails.getPrincipal();
            return userDt.getId();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUsername() {
        try {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDt = (UserDetails) userDetails.getPrincipal();
            return userDt.getUsername();
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer getUserEntity() {
        try {
            Authentication userDetails = SecurityContextHolder.getContext().getAuthentication();
            UserDetailsImpl userDt = (UserDetailsImpl) userDetails.getPrincipal();
            return userDt.getEntity();
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUsernameFromToken(HttpServletRequest httpServletRequest){
        return jwtUtils.getUserNameFromJwtToken(jwtUtils.parseJwt(httpServletRequest));
    }

    public static Integer getPositionFromToken(HttpServletRequest httpServletRequest){
        return jwtUtils.getPositionFromJwtToken(jwtUtils.parseJwt(httpServletRequest));
    }

    public static Integer getEntityFromToken(HttpServletRequest httpServletRequest){
        return jwtUtils.getEntityFromJwtToken(jwtUtils.parseJwt(httpServletRequest));
    }

    public static String getToken(HttpServletRequest httpServletRequest){
        return jwtUtils.getToken(httpServletRequest);
    }

    public static String generateFileName(String fileName) {
        try {
            String result = fileName.replaceAll("\\s", "");
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("ddmmyyyyHHmmss");

            return myDateObj.format(myFormatObj) + "_" + result;
        } catch (Exception e) {
            return null;
        }
    }
}
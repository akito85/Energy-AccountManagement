package com.dbs.common.library.config.jwt;

import com.dbs.common.base.utils.ResponseUtils;
import com.dbs.common.library.UserDetailsServiceImpl;
import com.dbs.common.library.ctrl.ResponseObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import org.springframework.http.HttpStatus;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter{

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static final String[] listCheckHealth = {
            "/v1/dbs/api/system-master/health",
            "/v1/dbs/api/system-rbi/health",
            "/v1/dbs/api/system-payment/health"
    };

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String x = "/v1/dbs/api/auth";
        String y = request.getRequestURI();
        String xy = y.substring(0, y.lastIndexOf("/"));
        String username = "";
        String jwt = "";
        JwtUtils tes = jwtUtils;
        boolean urlValidation = Arrays.stream(listCheckHealth).anyMatch(variable -> variable.equalsIgnoreCase(request.getRequestURI()));
        if(!urlValidation){
            if (!x.equalsIgnoreCase(xy)) {
                try {
                    jwt = jwtUtils.parseJwt(request);
                    if (jwt != null && tes.validateToken_(jwt)) {
                        username = tes.getUserNameFromJwtToken(jwt);
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        request.setAttribute("startTime", System.currentTimeMillis());
                        request.setAttribute("username", username);
                        request.setAttribute("token", jwt);
                        filterChain.doFilter(request, response);
                    } else {
                        ObjectMapper objectMapper = new ObjectMapper();
                        ResponseObject error = new ResponseObject(ResponseUtils.SUCCESS_FALSE, 419,
                                ResponseUtils.MESSAGE_INVALID_REQ, "Invalid Request");
                        String jsonResponse = objectMapper.writeValueAsString(error);
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_BAD_GATEWAY);
                        response.getOutputStream().print(jsonResponse);
                    }
                } catch (IOException | ServletException | UsernameNotFoundException e) {
                    logger.error("Cannot set user authentication: {}", e);
                }
            } else {
                filterChain.doFilter(request, response);
            }
        }else{
            request.getRequestDispatcher(request.getRequestURI()).forward(request, response);
        }
    }

    private final Set<String> excludedPaths = Set.of(
            "/v1/dbs/api/file/download2",
            "/v1/dbs/api/mu/activate-user",
            "/v1/dbs/api/auth/check-expired"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request)
            throws ServletException {
        String path = request.getRequestURI();
        return excludedPaths.stream().anyMatch(path::startsWith);
    }
}

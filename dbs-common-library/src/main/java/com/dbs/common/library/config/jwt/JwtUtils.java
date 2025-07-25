package com.dbs.common.library.config.jwt;

import com.dbs.common.library.impl.UserDetailsImpl;
import com.dbs.common.library.security.CryptoSecurity;
import com.dbs.common.library.services.GlobalPropertiesService;
import com.dbs.database.crm.entities.usermanagement.R_GLOBAL_PROPERTIES_DTL;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    private static String KEY_SECRET = "8xds//H4wsYnk+yYleprfr3uajuXN9svF8jVz4OrllQ=hjk76klz//ok891=fflkjk4hjG7jsadJn";

    //@Value("${backend.app.jwt.expiration}")
    private int jwtExpirationMs;
    
    private static final String HEADERDBS = "dbs-access-token";
    private static final String HEADERACCESS = "access-token";
    private static final String HEADERAUTH = "Authorization";
    
    @Autowired
    private GlobalPropertiesService globalPropertiesService;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Encoders.BASE64.encode(KEY_SECRET.getBytes()).getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    public String generateTrueJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Integer entity = userPrincipal.getEntity();
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject((userPrincipal.getUsername()))
                        .setIssuedAt(new Date())
                        .setExpiration(new DateTime().plusMinutes(480).toDate())
                        .claim("ent", entity)
                        .claim("pos", null)
                        .signWith(getSigningKey())
                        .compact()
        );
    }

    public String generateTempJwtTokenUser(Authentication authentication, Integer entity) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject((userPrincipal.getUsername()))
                        .setIssuedAt(new Date())
                        .setExpiration(new DateTime().plusMinutes(5).toDate())
                        .claim("ent", entity)
                        .claim("pos", null)
                        .signWith(getSigningKey())
                        .compact()
        );
    }
    
    public String generateTempJwtTokenSu(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject((userPrincipal.getUsername()))
                        .setIssuedAt(new Date())
                        .setExpiration(new DateTime().plusMinutes(5).toDate())
                        .claim("ent", null)
                        .claim("pos", null)
                        .signWith(getSigningKey())
                        .compact()
        );
    }

    public String generateJwtToken(String username, Integer role, Integer entity, Integer position) {
        R_GLOBAL_PROPERTIES_DTL rGlobalProp = globalPropertiesService.getGlobalProperties("SECURITY", "SESSION_TIME");
        Integer session = 60;
        if (rGlobalProp != null) {
            session = Integer.parseInt(rGlobalProp.getGpdVal());
        }
        logger.info("username : {} , expired_at : {}", (username), new DateTime().plusMinutes(session).toDate());
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(new DateTime().plusMinutes(session).toDate())
                        .claim("ent", entity)
                        .claim("pos", position)
                        .claim("role", role)
                        .signWith(getSigningKey())
                        .compact()
        );
    }
    
    public String generateJwtTokenWithDelegate(String username, List<Integer> role, Integer entity, Integer position) {
        R_GLOBAL_PROPERTIES_DTL rGlobalProp = globalPropertiesService.getGlobalProperties("SECURITY", "SESSION_TIME");
        Integer session = 60;
        if (rGlobalProp != null) {
            session = Integer.parseInt(rGlobalProp.getGpdVal());
        }
        logger.info("username : {} , expired_at : {}", (username), new DateTime().plusMinutes(session).toDate());
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(new Date())
                        .setExpiration(new DateTime().plusMinutes(session).toDate())
                        .claim("ent", entity)
                        .claim("pos", position)
                        .claim("role", role)
                        .signWith(getSigningKey())
                        .compact()
        );
    }
    
    public Date getExpFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getExpiration();
    }

    public String getUserNameFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getSubject();
    }

    public Integer getEntityFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        return Integer.parseInt(Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("ent").toString());
    }

    public Integer getPositionFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        Object claim = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("pos");
        if (claim != null) {
            return Integer.parseInt(claim.toString());
        }
        return null;
    }

    public Integer getRoleFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        return Integer.parseInt(Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("role").toString());
    }
    
    public List<Integer> getRoleDelegateFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        List<Integer> x = (List<Integer>) Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().get("role");
        return x;
    }

    public Date getReqDateFromJwtToken(String token) {
        token = CryptoSecurity.decrypt(token);
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().getIssuedAt();
    }

    public boolean validateJwtToken(String authToken) {
        authToken = CryptoSecurity.decrypt(authToken);
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

    public Map<String, String> verifyJwtToken(String authToken) {
        Map<String, String> res = new HashMap<>();
        String result = "";
        authToken = CryptoSecurity.decrypt(authToken);
        try {
            Jwts.parser().setSigningKey(getSigningKey()).parseClaimsJws(authToken);
            result = "valid";
        } catch (SignatureException e) {
            result = "invalid";
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            result = "malformed";
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            result = "expire";
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            result = "UnsupportedJwtException";
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            result = "illegalArgs";
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        res.put("result", result);
        return res;
    }

    public String generateJwtTokenForPublic(String user) {
        return CryptoSecurity.encrypt(
                Jwts.builder()
                        .setSubject(user)
                        .setIssuedAt(new Date())
                        .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                        .signWith(SignatureAlgorithm.HS512, getSigningKey())
                        .compact()
        );
    }


    public String parseJwt(HttpServletRequest request) {
        String headerAuth = null;
        try {
            headerAuth = request.getHeader(HEADERDBS);
        } catch (Exception e) {
            try {
                headerAuth = request.getHeader(HEADERACCESS);
            } catch (Exception exception) {
                headerAuth = request.getHeader(HEADERAUTH);
            }
        }

        if (headerAuth == null) {
            headerAuth = request.getHeader(HEADERAUTH);
        }

        if (headerAuth == null) {
            headerAuth = request.getHeader(HEADERACCESS);
        }

        if (StringUtils.hasText(headerAuth)) {
            return headerAuth;
        }

        return null;
    }

    public String getToken(HttpServletRequest request) {
        String headerAuth = null;
        try {
            headerAuth = request.getHeader(HEADERDBS);
        } catch (Exception e) {
            try {
                headerAuth = request.getHeader(HEADERACCESS);
            } catch (Exception exception) {
                headerAuth = request.getHeader(HEADERAUTH);
            }
        }

        if (headerAuth == null) {
            headerAuth = request.getHeader(HEADERAUTH);
        }

        if (headerAuth == null) {
            headerAuth = request.getHeader(HEADERACCESS);
        }

        return headerAuth;
    }

    public boolean validateToken(String token) {
        boolean result = false;
        if (token != null && !token.isEmpty()) {
            Date dateNow = new Date();
            Timestamp timestampNow = new Timestamp(dateNow.getTime());
            long diffInMiliSecond = Math.abs(timestampNow.getTime() - getExpFromJwtToken(token).getTime());
            long diffInMinute = diffInMiliSecond / 1000 / 60;
            if ((diffInMinute >= 0) && (diffInMinute <= 480)) {
                return true;
            }
        }
        return result;
    }

    public boolean validateToken_(String token) {
        if (token != null && !token.isEmpty()) {
            Date dateNow = new Date();
            Timestamp timestampNow = new Timestamp(dateNow.getTime());
            long diffInMiliSecond = Math.abs(timestampNow.getTime() - getExpFromJwtToken(token).getTime());
            long diffDays = TimeUnit.DAYS.convert(diffInMiliSecond, TimeUnit.MILLISECONDS);
            long diffMin = TimeUnit.MINUTES.convert(diffInMiliSecond, TimeUnit.MILLISECONDS);
            if ((diffDays == 0) && (diffMin <= 5)) {
                return false;
            }
        }
        return true;
    }
    
//    public String reGenerateTokenExpired(String token){
//        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody().setExpiration(new Date());
//    }
}

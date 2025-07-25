package com.dbs.common.library.impl;

import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.view.VW_T_USER_GROUPACCESS;
import com.dbs.database.crm.entities.usermanagement.view.VW_USER_GROUPACCESS;
import com.dbs.database.crm.repositories.usermanagement.view.VWTUserGroupAccRepo;
import com.dbs.database.crm.repositories.usermanagement.view.VWUserGroupAccRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class LdapUserDetailsImpl implements LdapUserDetails {
    @Autowired
    private VWTUserGroupAccRepo vwtUserGroupAccRepo;

    @Autowired
    private VWUserGroupAccRepo vwUserGaRepo;
    
    private String dn;
    private String username;
    private String password;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;

    public LdapUserDetailsImpl(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public LdapUserDetailsImpl() {
    }

    public LdapUserDetailsImpl build(M_USER user){
        List<String> aa = new ArrayList<>();
        Date today = new Date();
        Optional<List<VW_T_USER_GROUPACCESS>> tGa = vwtUserGroupAccRepo.findAllByUserIdAndStartDateIsLessThanEqualAndEndDateGreaterThanEqual
                (user.getUserId(), today, today);
        if (tGa.isPresent()) {
            Optional<VW_T_USER_GROUPACCESS> gaId = vwtUserGroupAccRepo.findByUserId(user.getUserId());

            Optional<List<VW_USER_GROUPACCESS>> vwGa = vwUserGaRepo.findAllByGaId(gaId.isPresent() ? gaId.get().getGaId() : null);
            vwGa.get().stream().forEach(
                    ac -> {
                        aa.add(ac.getAccessCode());
                    });
        }

//        List<GrantedAuthority> authorities = new ArrayList<String>(aa).stream()
//                .map(prev -> new SimpleGrantedAuthority(prev))
//                .collect(Collectors.toList());

        return new LdapUserDetailsImpl(
                user.getUsername(),
                user.getPassword());
    }

    @Override
    public String getDn() {
        return dn;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void eraseCredentials() {
        password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
}


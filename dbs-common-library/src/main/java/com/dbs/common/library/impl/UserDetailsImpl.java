package com.dbs.common.library.impl;

import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.entities.usermanagement.view.VW_T_USER_GROUPACCESS;
import com.dbs.database.crm.entities.usermanagement.view.VW_USER_GROUPACCESS;
import com.dbs.database.crm.repositories.usermanagement.view.VWTUserGroupAccRepo;
import com.dbs.database.crm.repositories.usermanagement.view.VWUserGroupAccRepo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;
    @Autowired
    private VWTUserGroupAccRepo vwtUserGroupAccRepo;

    @Autowired
    private VWUserGroupAccRepo vwUserGaRepo;

    private String id;

    private String username;

    private String email;

    @JsonIgnore
    private String password;

    private Integer entity;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String username, String email, Integer entity, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.entity = entity;
        this.authorities = authorities;
    }

    public UserDetailsImpl() {
    }

    public UserDetailsImpl build(M_USER user) {
//        List<String> aa = new ArrayList<>();
        Date today = new Date();
//        Optional<List<VW_T_USER_GROUPACCESS>> tGa = vwtUserGroupAccRepo.findAllByUserIdAndStartDateIsLessThanEqualAndEndDateGreaterThanEqual
//                (user.getUserId(), today, today);
//        if (tGa.isPresent()) {
//            Optional<VW_T_USER_GROUPACCESS> gaId = vwtUserGroupAccRepo.findByUserId(user.getUserId());
//
//            Optional<List<VW_USER_GROUPACCESS>> vwGa = vwUserGaRepo.findAllByGaId(gaId.isPresent() ? gaId.get().getGaId() : null);
//            vwGa.get().stream().forEach(
//                    ac -> {
//                        aa.add(ac.getAccessCode());
//                    });
//        }

        List<GrantedAuthority> authorities = new ArrayList<>();
//        new ArrayList<String>(aa).stream()
//                .map(prev -> new SimpleGrantedAuthority(prev))
//                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getUserId().toString(),
                user.getUsername(),
                user.getEmail(),
                user.getEntityId(),
                user.getPassword(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getEntity() {
        return entity;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}

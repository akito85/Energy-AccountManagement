package com.dbs.common.library;

import com.dbs.common.library.impl.UserDetailsImpl;
import com.dbs.database.crm.entities.usermanagement.M_USER;
import com.dbs.database.crm.repositories.usermanagement.MUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MUserRepo userRepository;

    @Autowired
    private UserDetailsImpl userDetailsImpl;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        M_USER user = userRepository.findByUsernameIgnoreCase(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
        return userDetailsImpl.build(user);
    }
}

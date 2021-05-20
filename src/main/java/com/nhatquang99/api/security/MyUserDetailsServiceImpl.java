package com.nhatquang99.api.security;

import com.nhatquang99.api.model.User;
import com.nhatquang99.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class MyUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = repository.findByUsername(s).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("Could not find by user!");
        }
        return new MyUserDetails(user);
    }
}

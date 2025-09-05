package com.utube.utube.service;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.utube.utube.entity.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User dbUser = this.userService.getUser(username);
        if (dbUser != null) {
            return new org.springframework.security.core.userdetails.User(dbUser.getUsername(), dbUser.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority(dbUser.getRole())));
        }
        throw new UsernameNotFoundException("User Not found");
    }

}

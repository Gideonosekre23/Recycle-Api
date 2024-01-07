package com.Ossolutions.RecycleApi.security;

import com.Ossolutions.RecycleApi.Model.User;
import com.Ossolutions.RecycleApi.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found for username: " + username);
        }

        User user = optionalUser.get();
        // Add logging
        System.out.println("User loaded from database: " + user);

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                getAuthorities(user.getUserType())
        );
    }


    private Collection<? extends GrantedAuthority> getAuthorities(String userType) {
        // Assuming that userType represents roles
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userType));
    }
}

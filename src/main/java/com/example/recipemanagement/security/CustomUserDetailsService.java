package com.example.recipemanagement.security;

import com.example.recipemanagement.entity.User;
import com.example.recipemanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(()-> new UsernameNotFoundException("User with email " + email + " not found"));
        return new UserPrincipal(user);
    }
}

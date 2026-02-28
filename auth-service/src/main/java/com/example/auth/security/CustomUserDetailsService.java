package com.example.auth.security;

import com.example.auth.entity.UserCredentials;
import com.example.auth.repo.UserCredentialsRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserCredentialsRepo userCredentialsRepo;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        UserCredentials user = userCredentialsRepo.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username: " + username)
                );
        return UserPrincipal.create(user);
    }
}

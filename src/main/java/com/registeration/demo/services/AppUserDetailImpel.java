package com.registeration.demo.services;

import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppUserDetailImpel implements UserDetailsService {

    private static final String USER_NOT_FOUND_MSG = "user with username or email of %s not found";
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserApp> userOptional = userRepository.findByUsername(username);
        if (!userOptional.isPresent()) {
            log.error("User {} not found in database !", username);
            throw  new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, username));
        }

        UserApp appUser = userOptional.get();

        return org.springframework.security.core.userdetails.User//
                .withUsername(username)//
                .password(appUser.getPassword())//
                .authorities(appUser.getAuthorities())//
                .accountExpired(appUser.getAccountExpired())//
                .accountLocked(!appUser.isAccountNonLocked())//
                .credentialsExpired(appUser.getCredentialsExpired())//
                .disabled(!appUser.getEnabled())//
                .build();
    }
}

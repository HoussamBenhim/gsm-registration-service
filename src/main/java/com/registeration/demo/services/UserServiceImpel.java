package com.registeration.demo.services;

import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpel implements UserService{
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserApp getUser(String userName) {
        log.info("Fetching user {}",userName );
        return userRepository.findByUsername(userName).isPresent() ? userRepository.findByUsername(userName).get() : null;
    }

    @Override
    public UserApp saveUser(UserApp user) {
        log.info("Saving user {} in database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()) );
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        return null;
    }

    @Override
    public void addRoleToUser(String userName, String roleName) {

    }

    @Override
    public List<UserApp> getUsers() {
        return null;
    }

    @Override
    public Set<String> refreshToken(String refreshToken) {
        return null;
    }
}

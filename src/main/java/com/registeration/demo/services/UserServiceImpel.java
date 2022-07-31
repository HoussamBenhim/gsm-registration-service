package com.registeration.demo.services;

import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
@Slf4j

@Transactional
@Configuration
public class UserServiceImpel implements UserService{
    @Autowired
    private  UserRepository userRepository;

    @Override
    public UserApp getUser(String userName) {
        log.info("Fetching user {}",userName );
        return userRepository.findByUsername(userName).isPresent() ? userRepository.findByUsername(userName).get() : null;
    }

    @Override
    public UserApp saveUser(UserApp user) {
        log.info("Saving user {} in database", user.getUsername());
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()) );
        return userRepository.save(user);
    }

    @Override
    public void addRoleToUser(String username, Role roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        UserApp user = userRepository.findByUsername(username).isPresent()?userRepository.findByUsername(username).get() : null ;
        if(user!=null) user.setRole(roleName);
    }

    @Override
    public List<UserApp> getUsers() {
        log.info("Fetching all users {}");
        return userRepository.findAll();
    }
    @Override
    public int enableUser(String username){
        return userRepository.enableAppUser(username);
    }


}

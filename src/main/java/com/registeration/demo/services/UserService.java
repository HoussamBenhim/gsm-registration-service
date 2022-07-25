package com.registeration.demo.services;

import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


public interface UserService {
    UserApp getUser(String userName);
    UserApp saveUser(UserApp user);
    Role saveRole(Role role);
    void addRoleToUser(String userName, String roleName);
    List<UserApp> getUsers();
    Set<String> refreshToken(String refreshToken);
}

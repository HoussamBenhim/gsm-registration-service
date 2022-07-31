package com.registeration.demo.services;

import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;


public interface UserService {
    UserApp getUser(String userName);
    UserApp saveUser(UserApp user);
    void addRoleToUser(String userName, Role roleName);
    List<UserApp> getUsers();
    int enableUser(String username);

}

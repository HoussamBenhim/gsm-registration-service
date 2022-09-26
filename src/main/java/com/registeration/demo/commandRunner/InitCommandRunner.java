package com.registeration.demo.commandRunner;

import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.UserRepository;
import com.registeration.demo.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class InitCommandRunner implements CommandLineRunner {

    private  final UserService userService;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
            UserApp user= new UserApp("houssam", "benhim","benhim.houssam@gmail.com", "ag7gDD35" , Role.ROLE_ADMIN);
        userService.saveUser(user);
        int enabled =userRepository.enableAppUser("benhim.houssam@gmail.com");
    }
}

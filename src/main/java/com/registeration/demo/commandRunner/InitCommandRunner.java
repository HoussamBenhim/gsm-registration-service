package com.registeration.demo.commandRunner;

import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class InitCommandRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        UserApp user= new UserApp("houssam", "benhim","benhim.houssam@gmail.com", "ag7gDD35" );
        userRepository.save(user);
    }
}

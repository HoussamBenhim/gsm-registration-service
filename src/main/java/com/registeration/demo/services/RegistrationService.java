package com.registeration.demo.services;

import com.registeration.demo.Exceptions.CustomException;
import com.registeration.demo.domain.RegistrationRequest;
import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final EmailService emailService;
    private final UserService userService;


    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailService.test(request.getEmail());
        if (!isValidEmail) {
            throw new CustomException("email not valid", HttpStatus.NOT_ACCEPTABLE);
        }
        signUpUser(new UserApp(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                Role.ROLE_USER
        ));

        return null;
    }

    private void signUpUser(UserApp user) {
        boolean userExists = userService.getUser(user.getUsername()) != null ? true : false;
        //if exists
        if (userExists) {
            //Check if there is a user with the same username
            UserApp existingUser = userService.getUser(user.getUsername());
            if(existingUser!=null){
                // Check if all attributes are the same
                if (existingUser.getFirstName().equals(user.getFirstName())
                        && existingUser.getLastName().equals(user.getLastName())
                        && new BCryptPasswordEncoder().encode(existingUser.getPassword()).equals(user.getPassword())) {
                    // if SO then check if email not confirmed
                    //TODO send a confirmation Email
                    throw  new CustomException("Email already exists!", HttpStatus.LOCKED);
                }
            }
            userService.saveUser(user);
            String token = UUID.randomUUID().toString();


        }
    }

}

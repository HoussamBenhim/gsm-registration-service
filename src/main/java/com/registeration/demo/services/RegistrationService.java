package com.registeration.demo.services;

import com.registeration.demo.Exceptions.CustomException;
import com.registeration.demo.domain.ConfirmationToken;
import com.registeration.demo.domain.RegistrationRequest;
import com.registeration.demo.domain.Role;
import com.registeration.demo.domain.UserApp;
import com.registeration.demo.repositories.ConfirmationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegistrationService {
    @Value("${application.url}")
    private String localhost;
    private final EmailService emailService;
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public RegistrationService(EmailService emailService, UserService userService, ConfirmationTokenService confirmationTokenService) {
        this.emailService = emailService;
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public String register(RegistrationRequest request) {
        boolean isValidEmail = emailService.test(request.getEmail());
        if (!isValidEmail) {
            throw new CustomException("email not valid", HttpStatus.NOT_ACCEPTABLE);
        }
        String token = signUpUser(new UserApp(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                Role.ROLE_USER
        ));
        String confirmationLink = localhost + "/registration/confirm?token=" + token;
        emailService.sendEmail("hbenhim@sequantis.com", emailService.buildEmail(request.getFirstName(), confirmationLink));
        return token;
    }

    private String signUpUser(UserApp user) {
        boolean userExists = userService.getUser(user.getUsername()) != null ? true : false;
        //if exists
        if (userExists) {
            //Check if there is a user with the same username
            UserApp existingUser = userService.getUser(user.getUsername());
            if (existingUser != null) {
                // Check if all attributes are the same
                if (existingUser.getFirstName().equals(user.getFirstName())
                        && existingUser.getLastName().equals(user.getLastName())
                        && new BCryptPasswordEncoder().encode(existingUser.getPassword()).equals(user.getPassword())) {
                    // if SO then check if email not confirmed
                    //TODO send a confirmation Email
                    throw new CustomException("Email already exists!", HttpStatus.LOCKED);
                }
            }
        }
        userService.saveUser(user);
        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                user
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public String confirmToken(String token) {
        // Check if token exist
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new CustomException("token not found", HttpStatus.NOT_FOUND));

        //check if token not expired
        LocalDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            new CustomException("token expired", HttpStatus.NOT_ACCEPTABLE);
        }
        //check if token not already confirmed
        int confirmed = confirmationTokenService.setConfirmedAt(token);
        // Confirm token
        userService.enableUser(confirmationToken.getUser().getUsername());
        return "confirmed";
    }
}



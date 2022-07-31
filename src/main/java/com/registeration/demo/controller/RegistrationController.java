package com.registeration.demo.controller;

import com.registeration.demo.domain.RegistrationRequest;
import com.registeration.demo.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;
    @PostMapping("/signup")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }
    @GetMapping("/confirm")
    public ResponseEntity<?> confirmToken(@RequestParam("token") String token){
        String confirmed = registrationService.confirmToken(token);
        return ResponseEntity.ok().body(confirmed);
    }
}

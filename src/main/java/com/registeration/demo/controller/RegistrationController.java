package com.registeration.demo.controller;

import com.registeration.demo.domain.RegistrationRequest;
import com.registeration.demo.services.RegistrationService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/registration")
@Slf4j
public class RegistrationController {
   // private final RestTemplate template;
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
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class Student{
        private Long id;
        private String lastname;
        private String firstname;
    }
}
//
//@Data @AllArgsConstructor @NoArgsConstructor
//class UserRequest{
//    private  String firstName;
//    private  String lastName;
//    private  String email;
//    private  String password;
//}
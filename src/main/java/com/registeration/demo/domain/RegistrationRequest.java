package com.registeration.demo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@EqualsAndHashCode
@ToString
public class RegistrationRequest {
    private String firstname;
    private String lastname;

    private  String email;
    private String password;

    public RegistrationRequest(){

    }
    public RegistrationRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}

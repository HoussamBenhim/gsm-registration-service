package com.registeration.demo.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.persistence.*;
import javax.transaction.Transactional;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

@Getter @Setter
@Entity
@NoArgsConstructor
@Service
@Transactional
@Table(name = "users")
public class UserApp implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "users_sequence",
            sequenceName = "users_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "users_sequence"
    )
    private Integer id;

    @GenericGenerator(name = "UUIDGenerator", strategy = "uuid2")
    @GeneratedValue(generator = "UUIDGenerator", strategy = GenerationType.AUTO)
    @Column(name = "uuid", updatable = false,nullable = false)
    private UUID uuid =UUID.randomUUID();

    @Size(min = 3, max = 255, message = "Minimum firstName length: 3 characters")
    @Column(nullable = false, name = "firstname")
    private String firstName;

    @Size(min = 3, max = 255, message = "Minimum lastName length: 3 characters")
    @Column(nullable = false, name = "lastname")
    private String lastName;

    @Size(min =4, max = 255, message = "Minimum username length: 4 characters")
    @Column(nullable = false, name = "username")
    private String username; // Or e-mail

    @Size(min =8, max = 255, message = "Minimum password length: 8 characters")
    @Column(nullable = false, name = "password")
    private String password;
    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false, name = "locked")
    private Boolean locked = false;

    @Column(nullable = false, name = "enabled")
    private Boolean enabled = false;

    @Column(nullable = false, name = "accountExpired")
    private Boolean accountExpired = false;

    @Column(nullable = false, name = "credentialsExpired")
    private Boolean credentialsExpired = false;

    public  UserApp(String firstName,String lastName, String username, String password, Role role  ){
        this.firstName=firstName;
        this.lastName= lastName;
        this.username= username;
        this.password=password;
        this.role= role;
    }
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !accountExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}

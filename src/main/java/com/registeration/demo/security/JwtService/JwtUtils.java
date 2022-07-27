package com.registeration.demo.security.JwtService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.registeration.demo.Exceptions.CustomException;
import com.registeration.demo.domain.Role;
import com.registeration.demo.services.AppUserDetailImpel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class JwtUtils {

    @Value("$spring.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    @Autowired
    private AppUserDetailImpel myUserDetailService;


    public String createToken(String username, Collection<GrantedAuthority> appUserRoles, String issuer) {
        return JWT.create().withSubject(username)
                .withClaim("role", appUserRoles.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .withIssuer(issuer)
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public String refreshToken(String username, String issuer) {
        return JWT.create().withSubject(username)
                .withIssuedAt(new Date())
                .withIssuer(issuer)
                .withExpiresAt(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .sign(Algorithm.HMAC256(secretKey.getBytes()));
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = myUserDetailService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        boolean isValidToken = validateToken(token);
        return isValidToken ? JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build().verify(token).getSubject() : null;
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build();
            DecodedJWT decodedJWT = verifier.verify(token);
        } catch (JWTVerificationException | IllegalArgumentException e) {
            new CustomException("Expired or invalid Token", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return true;
    }

    public Collection<SimpleGrantedAuthority> getAuthorities(String token) {
        String[] roles = JWT.require(Algorithm.HMAC256(secretKey.getBytes())).build().verify(token).getClaim("role").asArray(String.class);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Stream.of(roles).forEach(role ->
        {
            authorities.add(new SimpleGrantedAuthority(role));
        });
        return authorities;
    }

    public void writeOutputStream(HttpServletResponse response, String header_name_1, String header_name_2, String value_header_1, String value_header_2) throws IOException, ServletException {
        Map<String, String> outputs = new HashMap<>();
        outputs.put(header_name_1, value_header_1);
        outputs.put(header_name_2, value_header_2);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), outputs);
    }

}

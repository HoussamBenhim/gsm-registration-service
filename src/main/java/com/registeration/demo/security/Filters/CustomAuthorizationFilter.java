package com.registeration.demo.security.Filters;


import com.registeration.demo.security.JwtService.JwtUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;



@Slf4j
@RequiredArgsConstructor
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/registration/login") ||
                request.getServletPath().equals("/refreshToken") ||
                request.getServletPath().equals("/registration/signup") ||
                request.getServletPath().equals("/registration/confirm") ||
                request.getServletPath().startsWith("/registration/student") ||
                request.getServletPath().equals("/favicon.ico")||
                request.getServletPath().equals("/actuator/health")
        ) {
            filterChain.doFilter(request, response);
        } else {
            log.info("Processing Authorization .... ");
            String token = jwtUtils.resolveToken(request);
            if (token != null) {
                boolean isValidToken = jwtUtils.validateToken(token);
                if (isValidToken) {
                    try {
                        String username = jwtUtils.getUsername(token);
                        log.info("logging with username : {}", username);
                        Collection<SimpleGrantedAuthority> authorities = jwtUtils.getAuthorities(token);
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(username, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        filterChain.doFilter(request, response);
                    } catch (Exception e) {
                        log.error("error occurred at authorization process : {}", e.getMessage());
                        response.setHeader("ERROR", e.getMessage());
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        jwtUtils.writeOutputStream(response, "error_message", "error", e.getMessage(), "error at logging");
                    }
                }
            }else{
                log.info("Token absent!");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                jwtUtils.writeOutputStream(response, "error_message", "error", "Token Absent", "error at logging");
            }
        }
    }
}

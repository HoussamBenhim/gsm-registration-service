package com.registeration.demo.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Properties;

@Configuration
public class ConfigurationCalss {
    @Value("${spring.mail.host}")
    private String HOST;
    @Value("${spring.mail.port}")
    private int PORT;
    @Value("${spring.mail.username}")
    private String USER_NAME;
    @Value("${spring.mail.password}")
    private String PASSWORD;

    @Value("${spring.mail.protocol}")
    private String PROTOCOL;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(HOST);
        mailSender.setPort(PORT);
        mailSender.setProtocol(PROTOCOL);
        mailSender.setUsername(USER_NAME);
        mailSender.setPassword(PASSWORD);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        return mailSender;
    }

    @Bean @LoadBalanced WebClient.Builder builder(){
        return WebClient.builder();
    }
    @Bean
    WebClient webClient(WebClient.Builder builder) {
        return builder.build();
    }
//    @LoadBalanced
//    @Bean
//    public RestTemplate getRestTemplate(){
//        return new RestTemplate();
//    }
//


}

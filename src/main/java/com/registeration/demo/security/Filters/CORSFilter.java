package com.registeration.demo.security.Filters;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Component
@Order(1)
@Slf4j
public class CORSFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CORSFilter.class);
    @Value("${front.end.cros.url}")
    private String FRONT_END_URL;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("from CROS filter url value : {}", FRONT_END_URL);
        HttpServletRequest httprequest = (HttpServletRequest) request;
        log.info("headers : {}", httprequest.getHeaderNames());
        HttpServletResponse httpresponse = (HttpServletResponse) response;
        httpresponse.setHeader("Access-Control-Allow-Origin", FRONT_END_URL);
        httpresponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, DELETE");
        httpresponse.setHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, *");
        httpresponse.setHeader("Access-Control-Max-Age", "3600");
        httpresponse.setHeader("Access-Control-Expose-Headers", "*");
        chain.doFilter(request, response);

    }
}

package com.example.mcp.config.security;

import java.io.IOException;
import java.text.ParseException;

import com.nimbusds.jwt.JWTParser;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
public class TokenLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            var token = authorizationHeader.substring(7);
            log.info("TOKEN: {}", token);

            try {
                var aud = JWTParser.parse(token).getJWTClaimsSet().getAudience();
                log.info("AUDIENCE: {}", aud);
            } catch (ParseException e) {
                log.warn("Can't extract AUDIENCE", e);
            }
        }

        filterChain.doFilter(request, response);
    }

}

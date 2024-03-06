package com.tpe.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTUtils jwtUtils;


    // token filtreden geçirilecek, requestten token'ı çekmemiz gerekiyor.

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = parseJwt(request);

        try {
            if (token != null && jwtUtils.validateToken(token)) { // token null değilse ve tokenimiz başarılı şekilde valide edildiyse o zaman userımız eşleşir.


                // Bu user SecurityContext'e konacak.
                String username = jwtUtils.getUsernameFromJwtToken(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // login olan user'ın userdetails hali.


                // bu user'ı contex'e koyacağız. Authentication objesi oluşturulmalı. Alttaki class Authentication classını implemente eder. Constructor'ında objemizi, credentialsı ve authatiry leri girdik. credentials'a genelde password girilir.
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken); // setAuthentication methodu Authentication tipinde değişken kabul ediyor.

            }
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
        }

        // filtre tamamlandıktan sonra alttaki komut çağırılır.

        filterChain.doFilter(request, response); // filtreleme requeste ve response aktarılır.
    }

    // requestten tokeni çeken method
    private String parseJwt(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;

    }
}

package com.tpe.security.config;

import com.tpe.security.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Method bazlı olarak erişim verebilmek için aktif eddilmelidir.
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    /*
     * http request ayarları
     * provider ayarları yapılacak : userDetail ve password encode ayarları yapılacak
     * */

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthTokenFilter authTokenFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().
                disable().
                sessionManagement().
                sessionCreationPolicy(SessionCreationPolicy.STATELESS). // session yönetimi olmadığı için stateless seçtik.
                and().
                authorizeHttpRequests().
                antMatchers("/register", "/login").
                permitAll().
                anyRequest().
                authenticated();

        http.addFilterBefore(authTokenFilter , UsernamePasswordAuthenticationFilter.class); // security nin filtresinden önce kendi
                                                                                            // filtreme tabi tut. ilk parametre
                                                                                            // kendi filtrem ikincisi securitynin filtresi.
    }


    // provider configure etmeden authentication manager'ı doğrudan configure edebiliriz.
    @Bean // Uygulama içinde kullandığımız için bean ile anote ettik.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(); //default değeri 10 dur. range'i 1 - 34;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean // Uygulama içerisinde AuthenticationManager objesi lazım olacak
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
}

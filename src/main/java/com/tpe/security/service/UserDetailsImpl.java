package com.tpe.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpe.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsImpl implements UserDetails { // UserDetails tanımlamış olduk.
    private Long id;

    private String username;

    @JsonIgnore // Json formatına maplemek gerekirse password json da yazılmaz.
    private String password;

    private Collection<? extends GrantedAuthority> authorities; // security de roller yoktu grantedAuthority vardı.


    public static UserDetailsImpl build(User user){
        //SimpleSrantedAuthority GrantedAuthority'yi extend eden bir classdır.

        List<SimpleGrantedAuthority> authorities=user.getRoles().stream().
                map(role->new SimpleGrantedAuthority(role.getType().name())).collect(Collectors.toList());
        // yukarıdaki aynı işlem basic authentication'da da yapılmıştı.

        return new UserDetailsImpl(user.getId(),user.getUserName(),user.getPassword(),authorities);
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


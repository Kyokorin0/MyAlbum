package com.kyoko.myalbum.filter.Util;

import com.kyoko.myalbum.dao.MyUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author young
 * @create 2023/3/14 17:48
 * @Description
 */
@Configuration
@RequiredArgsConstructor
public class SecurityServiceUtil {
    //和MyUserDetailsUtil.class等效
    private final MyUserRepo myUserRepo;
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> myUserRepo.findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException("User Not Found"));
    }
    @Bean
    //SecurityConfig
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setUserDetailsService(userDetailsService());
        daoAuthProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    //AuthenticationService
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

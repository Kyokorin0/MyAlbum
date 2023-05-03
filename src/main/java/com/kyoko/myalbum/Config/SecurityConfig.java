package com.kyoko.myalbum.Config;

import com.kyoko.myalbum.Filter.JwtAuthenticationFilter;
import com.kyoko.myalbum.Property.ProjProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author young
 * @create 2023/3/14 17:28
 * @Description
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final ProjProperties projProperties;
    private final JwtAuthenticationFilter JwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()//禁用crsf()
                .authorizeHttpRequests()
                .requestMatchers(projProperties.getSecurityWhiteList())
                .permitAll()//白名单：列表中的请求
                .anyRequest()
                .authenticated()//其他请求需要经过jwt过滤器
                .and()//添加新配置
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//会话创建策略，这里选用无状态
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterAfter(JwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();

    }
}

package com.project.config;

import com.project.service.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/login").permitAll()
                .antMatchers( "/user").hasRole("ADMIN")
                .antMatchers( "/user/password").authenticated()
                .antMatchers("/project", "/feature", "/task", "/issue", "/check-in").hasAnyRole("MANAGER", "USER")
                .antMatchers(HttpMethod.DELETE, "/user/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/user/**/update-role").hasRole("ADMIN")
                .anyRequest().authenticated()
//                .anyRequest().permitAll()

                .and()
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    }




}

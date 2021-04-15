package com.liverday.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;

@Configuration
@EnableWebSecurity
@SuppressWarnings("unused")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter  {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/foos/**").hasAuthority("SCOPE_read")
                .antMatchers(HttpMethod.POST, "/foos").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.PUT, "/foos").hasAuthority("SCOPE_write")
                .antMatchers(HttpMethod.DELETE, "/foos").hasAuthority("SCOPE_write")
                .anyRequest().authenticated()
                .and()
            .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);
    }
}

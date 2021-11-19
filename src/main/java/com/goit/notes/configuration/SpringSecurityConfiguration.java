package com.goit.notes.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SneakyThrows
    @Autowired
    public void configureGlobal(BCryptPasswordEncoder passwordEncoder, 
            UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        auth.inMemoryAuthentication()
                .withUser("admin")
                .password("12345678")
                .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        // Только для роли Admin
        http.authorizeRequests().antMatchers("/user/listUsers").access("hasRole('ROLE_ADMIN')");

        // config
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/user/register").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/j_spring_security_check")
                .defaultSuccessUrl("/note/listNotes") // поменять на правильный урл
                .failureUrl("/login?error=true")
                .usernameParameter("userName")
                .passwordParameter("password")
                .permitAll()
                // logout config
                .and()
                .logout()
                .logoutUrl("/logout");



    }

}
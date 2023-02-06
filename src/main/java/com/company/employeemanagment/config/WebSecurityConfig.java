package com.company.employeemanagment.config;

import com.company.employeemanagment.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/register").permitAll()
                .antMatchers("/homepage").permitAll()
                .antMatchers("/register-confirm").permitAll()
                .antMatchers("/successful").permitAll()
                .antMatchers("/error-info").permitAll()
                .antMatchers("/forget-password").permitAll()
                .antMatchers("/forget-password-confirm").permitAll()
                .antMatchers("/save-new-password").permitAll()
                .antMatchers("/employee-list").permitAll()
                .antMatchers("/insert").permitAll()
                .antMatchers("/employee-save").permitAll()
                .antMatchers("/edit-employee-page").permitAll()
                .antMatchers("/edit-employee").permitAll()
                .antMatchers("/edit-employee-save").permitAll()
                .antMatchers("/delete-employee-page").permitAll()
                .antMatchers("/delete-employee").permitAll()
                .antMatchers("/delete-employee-save").permitAll()
                .antMatchers("/resend").permitAll()
                .and().csrf().disable().formLogin()
                .loginPage("/login").loginProcessingUrl("/homepage")
                .usernameParameter("email")
                .passwordParameter("password")
                .and().logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").and().exceptionHandling();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/resources/**",
                        "/static/**",
                        "/css/**",
                        "/images/**",
                        "/js/**"
                );
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

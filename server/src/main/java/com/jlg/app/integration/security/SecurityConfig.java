package com.jlg.app.integration.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.SecurityProperties.ACCESS_OVERRIDE_ORDER;
import static org.springframework.http.HttpMethod.DELETE;


@Configuration
@EnableWebSecurity
@Order(ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsService userDetailsService;
  @Autowired
  AuthorizationErrorHandler authorizationErrorHandler;
  @Autowired
  AuthenticationErrorHandler authenticationErrorHandler;
  @Autowired
  LoginSuccessHandler loginSuccessHandler;
  @Autowired
  LoginFailureHandler loginFailureHandler;
  @Autowired
  HttpLogoutSuccessHandler logoutSuccessHandler;
  @Autowired
  SecurityConfigProperties securityConfigProperties;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new StandardPasswordEncoder();
  }

  @Bean
  CsrfTokenRepository csrfTokenRepository() {
    HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
    repository.setHeaderName("X-XSRF-TOKEN");
    return repository;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    //error handling
    http.exceptionHandling()
        .accessDeniedHandler(authorizationErrorHandler)
        .authenticationEntryPoint(authenticationErrorHandler);

    //authentication
    http.formLogin()
        .permitAll()
        .loginPage("/app/login")
        .loginProcessingUrl("/api/accounts/login")
        .usernameParameter("email")
        .passwordParameter("password")
        .successHandler(loginSuccessHandler)
        .failureHandler(loginFailureHandler)
        .and()
        .logout()
        .permitAll()
        .logoutRequestMatcher(new AntPathRequestMatcher("/api/accounts/logout", DELETE.name()))
        .logoutSuccessHandler(logoutSuccessHandler)
        .invalidateHttpSession(true);
    http.rememberMe().key(securityConfigProperties.getPersistentLoginKey());


    //authorization
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/assets/**",
            "/components/content",
            "/app",
            "/app/",
            "/app/login/**",
            "/app/register",
            "/app/recover-password",
            "/app/reset-password/**",
            "/app/settings/**",
            "/api/**",
            "/h2",
            "/h2/**",
            "/h2/login**"
        )
        .permitAll()
        .anyRequest()
        .authenticated();


    http.headers().frameOptions().disable();

    //session
    http.sessionManagement().maximumSessions(1);

    //cross site forgery protection
//    http.csrf().disable();
    http
        .csrf()
        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**"))
        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**/*"))
        .csrfTokenRepository(csrfTokenRepository())
        .and()
        .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }
}


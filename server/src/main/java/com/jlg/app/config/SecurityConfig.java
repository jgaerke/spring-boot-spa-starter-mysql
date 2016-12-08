package com.jlg.app.config;

import com.jlg.app.security.AuthenticationErrorHandler;
import com.jlg.app.security.AuthorizationErrorHandler;
import com.jlg.app.security.HttpLogoutSuccessHandler;
import com.jlg.app.security.LoginFailureHandler;
import com.jlg.app.security.LoginSuccessHandler;
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
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.session.data.mongo.config.annotation.web.http.EnableMongoHttpSession;

import static org.springframework.boot.autoconfigure.security.SecurityProperties.ACCESS_OVERRIDE_ORDER;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
@EnableMongoHttpSession
@Order(ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  public UserDetailsService userDetailsService;
  @Autowired
  public AuthorizationErrorHandler authorizationErrorHandler;
  @Autowired
  public AuthenticationErrorHandler authenticationErrorHandler;
  @Autowired
  public LoginSuccessHandler loginSuccessHandler;
  @Autowired
  public LoginFailureHandler loginFailureHandler;
  @Autowired
  public HttpLogoutSuccessHandler logoutSuccessHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new StandardPasswordEncoder("starter");
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
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
        .logoutRequestMatcher(new AntPathRequestMatcher("/api/accounts/logout", POST.name()))
        .logoutSuccessHandler(logoutSuccessHandler)
        .invalidateHttpSession(true);
    http.rememberMe().rememberMeParameter("rememberMe");


    //authorization
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/assets/**",
            "/components/content",
            "/app**",
            "/app/",
            "/app/**",
            "/partials/**",
            "/api/**",
            "/h2",
            "/h2/**",
            "/h2/login**"
        )
        .permitAll()
        .anyRequest()
        .authenticated();


//    http.headers().frameOptions().disable();

    //session
    http.sessionManagement().maximumSessions(1);

    //cross site forgery protection
    http.csrf().disable();
//    http
//        .csrf()
////        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**"))
////        .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/api/**/*"))
//        .csrfTokenRepository(csrfTokenRepository())
//        .and()
//        .addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
        .userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }
}


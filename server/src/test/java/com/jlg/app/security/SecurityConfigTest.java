package com.jlg.app.security;

import com.jlg.app.config.SecurityConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.userdetails
    .DaoAuthenticationConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SecurityConfigTest {

  private static String getHeaderName(CsrfTokenRepository csrfTokenRepository) throws IllegalAccessException,
      NoSuchFieldException {
    Field f = csrfTokenRepository.getClass().getDeclaredField("headerName"); //NoSuchFieldException
    f.setAccessible(true);
    return (String) f.get(csrfTokenRepository);
  }

  @Test
  public void should_use_standard_password_encoder() throws Exception {
    //given
    SecurityConfig securityConfig = new SecurityConfig();

    //when
    PasswordEncoder standardPasswordEncoder = securityConfig.passwordEncoder();

    //then
    assertEquals("Should be StandardPasswordEncoder.class instance.", StandardPasswordEncoder.class,
        standardPasswordEncoder.getClass());
  }

  @Test
  public void should_use_http_session_token_repository_and_set_expected_header() throws Exception {
    //given
    String headerName = "X-XSRF-TOKEN";
    SecurityConfig securityConfig = new SecurityConfig();

    //when
    CsrfTokenRepository csrfTokenRepository = securityConfig.csrfTokenRepository();

    //then
    assertSame("Should be HttpSessionCsrfTokenRepository.class instance.", HttpSessionCsrfTokenRepository.class,
        csrfTokenRepository.getClass());
    assertEquals("Should be set with expected header name .", headerName, getHeaderName(csrfTokenRepository));

  }

  @Test
  public void should_be_configured_with_expected_user_details_service_and_password_encoder() throws Exception {
    //given
    SecurityConfig securityConfig = new SecurityConfig();
    securityConfig.userDetailsService = mock(UserDetailsService.class);
    DaoAuthenticationConfigurer authenticationConfigurer = mock(DaoAuthenticationConfigurer.class);
    AuthenticationManagerBuilder authenticationManagerBuilder = mock(AuthenticationManagerBuilder.class);
    when(authenticationManagerBuilder.userDetailsService(any(UserDetailsService.class))).thenReturn
        (authenticationConfigurer);

    //when
    securityConfig.configure(authenticationManagerBuilder);

    //then
    verify(authenticationManagerBuilder).userDetailsService(eq(securityConfig.userDetailsService));
    verify(authenticationConfigurer).passwordEncoder(any(StandardPasswordEncoder.class));
  }
}
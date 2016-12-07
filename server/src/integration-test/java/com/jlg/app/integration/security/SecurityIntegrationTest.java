package com.jlg.app.integration.security;


import com.jlg.app.Application;
import com.jlg.app.model.Account;
import com.jlg.app.repository.AccountRepository;
import com.jlg.app.integration.support.MockPostProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

import static com.google.common.collect.ImmutableMap.of;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class, SecurityIntegrationTest.Config.class}, webEnvironment = RANDOM_PORT)
@ActiveProfiles("security-integration-test")
public class SecurityIntegrationTest {

  MockMvc mvc;

  @Autowired
  WebApplicationContext context;

  @Autowired
  AccountRepository accountRepository;

  @Autowired
  CsrfTokenRepository csrfTokenRepository;

  @Autowired
  FilterChainProxy filterChain;

  private DefaultCsrfToken token;

  @Before
  public void setUp() {
    reset(accountRepository);
    reset(csrfTokenRepository);
    this.mvc = webAppContextSetup(context).addFilters(filterChain).build();
    token = new DefaultCsrfToken("XSRF-TOKEN", "n/a", "bar");
    when(csrfTokenRepository.generateToken(any(HttpServletRequest.class))).thenReturn(token);
    clearContext();
  }

  @Test
  public void should_allow_root_requests() throws Exception {
    mvc.perform(get("/"))
        .andExpect(cookie().exists("XSRF-TOKEN"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_allow_app_login_requests() throws Exception {
    mvc.perform(get("/app/login"))
        .andExpect(header().string("Content-Type", "text/html;charset=UTF-8"))
        .andExpect(cookie().exists("XSRF-TOKEN"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_allow_app_password_recovery_requests() throws Exception {
    mvc.perform(get("/app/recover-password"))
        .andExpect(header().string("Content-Type", "text/html;charset=UTF-8"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_allow_app_registration_requests() throws Exception {
    mvc.perform(get("/app/register"))
        .andExpect(header().string("Content-Type", "text/html;charset=UTF-8"))
        .andExpect(cookie().exists("XSRF-TOKEN"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_allow_api_requests_that_provide_a_csrf_token() throws Exception {
    mvc.perform(get("/api/some-fake-end-point")
        .header("XSRF-TOKEN", "bar"))
        .andExpect(status().isNotFound())
        .andDo(print());
  }

  @Test
  public void should_disallow_api_requests_that_are_missing_a_csrf_token() throws Exception {
    mvc.perform(get("/api/some-fake-end-point"))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  public void should_not_allow_post_without_csrf_token() throws Exception {
    mvc.perform(post("/api/accounts/login"))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  public void should_not_allow_delete_without_csrf_token() throws Exception {
    mvc.perform(delete("/api/accounts/login"))
        .andExpect(status().isForbidden())
        .andDo(print());
  }


  @Test
  public void should_not_allow_put_without_csrf_token() throws Exception {
    mvc.perform(put("/api/accounts/login"))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  public void should_not_allow_patch_without_csrf_token() throws Exception {
    mvc.perform(patch("/api/accounts/login"))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  public void should_handle_successful_login() throws Exception {
    when(csrfTokenRepository.loadToken(any(HttpServletRequest.class))).thenReturn(token);
    when(accountRepository.findOneByEmail(anyString())).thenReturn(of(getAccount("some-email@gmail.com", "password")));
    mvc.perform(post("/api/accounts/login")
        .param("email", "some-email@gmail.com")
        .param("password", "password")
        .param("remember-me", "false")
        .header("XSRF-TOKEN", "bar")
        .contentType(APPLICATION_FORM_URLENCODED_VALUE))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_handle_invalid_email() throws Exception {
    when(csrfTokenRepository.loadToken(any(HttpServletRequest.class))).thenReturn(token);
    when(accountRepository.findOneByEmail(anyString())).thenReturn(empty());
    mvc.perform(post("/api/accounts/login")
        .param("email", "some-email@gmail.com")
        .param("password", "password")
        .param("remember-me", "false")
        .header("XSRF-TOKEN", "bar")
        .contentType(APPLICATION_FORM_URLENCODED_VALUE))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  public void should_handle_invalid_password() throws Exception {
    when(csrfTokenRepository.loadToken(any(HttpServletRequest.class))).thenReturn(token);
    when(accountRepository.findOneByEmail(anyString())).thenReturn(of(getAccount("some-email@gmail.com", "password2")));
    mvc.perform(post("/api/accounts/login")
        .param("email", "some-email@gmail.com")
        .param("password", "password")
        .param("remember-me", "false")
        .header("XSRF-TOKEN", "bar")
        .contentType(APPLICATION_FORM_URLENCODED_VALUE))
        .andExpect(status().isUnauthorized())
        .andDo(print());
  }

  @Test
  public void should_handle_remember_me() throws Exception {
    when(csrfTokenRepository.loadToken(any(HttpServletRequest.class))).thenReturn(token);
    when(accountRepository.findOneByEmail(anyString())).thenReturn(of(getAccount("some-email@gmail.com", "password")));
    mvc.perform(post("/api/accounts/login")
        .param("email", "some-email@gmail.com")
        .param("password", "password")
        .param("remember-me", "true")
        .header("XSRF-TOKEN", "bar")
        .contentType(APPLICATION_FORM_URLENCODED_VALUE))
        .andExpect(cookie().value("remember-me", notNullValue()))
        .andExpect(status().isOk())
        .andDo(print());
  }

  private Account getAccount(String email, String password) {
    return Account.builder()
        .email(email)
        .password(new StandardPasswordEncoder().encode(password))
        .first("jeremy")
        .last("gaerke")
        .build();
  }

  @Configuration
  @Profile("security-integration-test")
  public static class Config {
    @Bean
    public MockPostProcessor mockPostProcessor() {
      return new MockPostProcessor(
          of(
              "accountRepository", mock(AccountRepository.class),
              "csrfTokenRepository", mock(CsrfTokenRepository.class)
          )
      );
    }
  }
}

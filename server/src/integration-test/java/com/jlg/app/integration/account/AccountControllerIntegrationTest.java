package com.jlg.app.integration.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jlg.app.Application;
import com.jlg.app.model.Account;
import com.jlg.app.repository.AccountRepository;
import com.jlg.app.request.RegistrationRequest;
import com.jlg.app.integration.support.MockPostProcessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.jlg.app.TestUtil.*;
import static java.util.Optional.empty;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.security.core.context.SecurityContextHolder.clearContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes={Application.class,AccountControllerIntegrationTest.Config.class}, webEnvironment = RANDOM_PORT)
@ActiveProfiles("account-controller-integration-test")
public class AccountControllerIntegrationTest {
  private MockMvc mvc;

  @Autowired
  CsrfTokenRepository csrfTokenRepository;

  @Autowired
  UserDetailsService userDetailsService;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  Filter springSecurityFilterChain;

  private DefaultCsrfToken token;

  @Before
  public void setUp() {
    reset(accountRepository);
    reset(csrfTokenRepository);
    mvc = webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
    token = new DefaultCsrfToken("XSRF-TOKEN", "n/a", "bar");
    when(csrfTokenRepository.generateToken(any(HttpServletRequest.class))).thenReturn(token);
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User("some-email@gmail.com", "password",
        newArrayList()));
    clearContext();
  }


  @Test
  public void should_return_200_and_account_upon_creation() throws Exception {
    //given
    RegistrationRequest input = createValidRegistrationRequest();
    Account output = createValidExistingAccount();
    String request = objectMapper.writeValueAsString(input);
    when(accountRepository.findOneByEmail(anyString())).thenReturn(empty());
    when(accountRepository.save(any(Account.class))).thenReturn(output);

    //when
    ResultActions result = mvc.perform(
        post("/api/accounts")
            .contentType(APPLICATION_JSON_VALUE)
            .header("XSRF-TOKEN", "bar")
            .content(request)
    );

    //then
    result
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  public void should_return_403_and_error_when_csrf_token_not_provided() throws Exception {
    //given
    Account input = createValidNewAccount();
    Account output = input.withId(UUID.randomUUID());
    String request = objectMapper.writeValueAsString(input);
    when(accountRepository.save(any(Account.class))).thenReturn(output);

    //when
    ResultActions result = mvc.perform(
        post("/api/accounts").contentType(APPLICATION_JSON_VALUE).content(request)
    );

    //then
    result
        .andExpect(status().isForbidden())
        .andDo(print());
  }


  @Configuration
  @Profile("account-controller-integration-test")
  public static class Config {
    @Bean
    public MockPostProcessor mockPostProcessor() {
      return new MockPostProcessor(
          of(
              "accountRepository", mock(AccountRepository.class),
              "csrfTokenRepository", mock(CsrfTokenRepository.class),
              "userDetailsService", mock(UserDetailsService.class)
          )
      );
    }
  }
}

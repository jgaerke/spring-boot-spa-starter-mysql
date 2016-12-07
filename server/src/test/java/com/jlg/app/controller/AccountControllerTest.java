package com.jlg.app.controller;

import com.jlg.app.controller.AccountController;
import com.jlg.app.request.PasswordChangeRequest;
import com.jlg.app.request.PasswordRecoveryRequest;
import com.jlg.app.request.PasswordResetRequest;
import com.jlg.app.request.RegistrationRequest;
import com.jlg.app.service.AccountService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

import static com.google.common.collect.Lists.newArrayList;
import static com.jlg.app.TestUtil.createValidExistingAccount;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountControllerTest {

  @Mock
  AccountService accountService;

  @Mock
  UserDetailsService userDetailsService;

  @Mock
  private HttpServletRequest request;

  @InjectMocks
  AccountController accountController;

  @Before
  public void setup() {
    when(userDetailsService.loadUserByUsername(anyString())).thenReturn(new User("some-email@gmail.com", "password",
        newArrayList()));
  }

  @Test
  public void should_create_account() throws Exception {
    //given
    when(accountService.create(any(RegistrationRequest.class))).thenReturn(createValidExistingAccount());
    RegistrationRequest registrationRequest = new RegistrationRequest("some-email@gmail.com", "password");
    //when
    accountController.create(registrationRequest);
    //then
    verify(accountService).create(eq(registrationRequest));
  }

  @Test
  public void should_change_password() throws Exception {
    //given
    when(accountService.changePassword(any(PasswordChangeRequest.class), anyString())).thenReturn
        (createValidExistingAccount());
    Principal principal = mock(Principal.class);
    when(principal.getName()).thenReturn("some-email@gmail.com");
    when(request.getUserPrincipal()).thenReturn(principal);
    PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest("password");
    //when
    accountController.changePassword(passwordChangeRequest, request);
    //then
    verify(accountService).changePassword(eq(passwordChangeRequest), eq("some-email@gmail.com"));
  }

  @Test
  public void should_reset_password() throws Exception {
    //given
    when(accountService.resetPassword(any(PasswordResetRequest.class))).thenReturn
        (createValidExistingAccount());
    PasswordResetRequest passwordResetRequest = new PasswordResetRequest("token", "password");
    //when
    accountController.resetPassword(passwordResetRequest);
    //then
    verify(accountService).resetPassword(eq(passwordResetRequest));
  }

  @Test
  public void should_recover_password() throws Exception {
    //given
    when(accountService.resetPassword(any(PasswordResetRequest.class))).thenReturn
        (createValidExistingAccount());
    PasswordRecoveryRequest passwordRecoveryRequest = new PasswordRecoveryRequest("some-email@gmail.com");
    //when
    accountController.recoverPassword(passwordRecoveryRequest);
    //then
    verify(accountService).sendPasswordResetInstructions(eq("some-email@gmail.com"));
  }
}
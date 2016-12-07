package com.jlg.app.integration.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LoginSuccessHandlerTest {

  @Test
  public void should_handle_login_success() throws Exception {
    //given
    LoginSuccessHandler loginSuccessHandler = new LoginSuccessHandler();
    HttpServletResponse response = mock(HttpServletResponse.class);

    //when
    loginSuccessHandler.onAuthenticationSuccess(null, response, null);

    //then
    verify(response).setStatus(SC_OK);
  }
}
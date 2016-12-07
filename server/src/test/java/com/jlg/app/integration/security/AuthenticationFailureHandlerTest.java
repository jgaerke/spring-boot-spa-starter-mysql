package com.jlg.app.integration.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationFailureHandlerTest {

  @Test
  public void on_authentication_failure_should_return_unauthorized_status_code() throws Exception {
    //given
    LoginFailureHandler loginFailureHandler = new LoginFailureHandler();
    HttpServletResponse response = mock(HttpServletResponse.class);

    //when
    loginFailureHandler.onAuthenticationFailure(null, response, null);

    //then
    verify(response).setStatus(SC_UNAUTHORIZED);
  }


}
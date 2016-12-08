package com.jlg.app.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class HttpLogoutSuccessHandlerTest {

  @Test
  public void should_return_ok_status_on_logout_success() throws Exception {
    //given
    HttpLogoutSuccessHandler httpLogoutSuccessHandler = new HttpLogoutSuccessHandler();
    HttpServletResponse response = mock(HttpServletResponse.class);

    //when
    httpLogoutSuccessHandler.onLogoutSuccess(null, response, null);

    //then
    verify(response).setStatus(SC_OK);
  }
}
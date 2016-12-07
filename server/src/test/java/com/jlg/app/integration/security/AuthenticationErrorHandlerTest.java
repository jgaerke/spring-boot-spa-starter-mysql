package com.jlg.app.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.OutputStream;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationErrorHandlerTest {

  @Test
  public void commence_should_return_unuathorized_response_with_error_message() throws Exception {
    //given
    ObjectMapper objectMapper = mock(ObjectMapper.class);
    AuthenticationErrorHandler authenticationErrorHandler = new AuthenticationErrorHandler(objectMapper);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn("http://someplace.com/with/some/path");
    when(request.getContextPath()).thenReturn("/with/some/path");
    String message = "bad credentials";
    BadCredentialsException badCredentialsException = new BadCredentialsException(message);

    //when
    authenticationErrorHandler.commence(request, response, badCredentialsException);

    //then
    verify(response).setStatus(eq(SC_FORBIDDEN));
    verify(response).setContentType(APPLICATION_JSON_VALUE);
    verify(objectMapper).writeValue(any(OutputStream.class), any(SecurityErrorDetails.class));

  }
}
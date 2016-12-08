package com.jlg.app.security;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import static javax.servlet.http.HttpServletResponse.SC_OK;

@Component
public class HttpLogoutSuccessHandler implements LogoutSuccessHandler {
  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    response.setStatus(SC_OK);
  }
}

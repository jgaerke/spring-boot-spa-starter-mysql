package com.jlg.app.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthenticationErrorHandler implements AuthenticationEntryPoint {
  private static final Logger logger = LoggerFactory.getLogger(AuthenticationErrorHandler.class);
  private final ObjectMapper objectMapper;

  @Autowired
  public AuthenticationErrorHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
                       AuthenticationException authenticationException) throws IOException {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    logger.error(
        format(
            "error: %s path: %s",
            path,
            "Authentication required"
        ),
        authenticationException
    );
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(SC_FORBIDDEN);
    objectMapper.writeValue(response.getOutputStream(), new SecurityErrorDetails(
        UUID.randomUUID(),
        new Date(),
        UNAUTHORIZED,
        authenticationException,
        "Unauthorized",
        path
    ));
  }
}

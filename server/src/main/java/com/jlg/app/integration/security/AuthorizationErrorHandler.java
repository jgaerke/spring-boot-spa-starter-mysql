package com.jlg.app.integration.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import static java.lang.String.format;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class AuthorizationErrorHandler implements AccessDeniedHandler {
  private static final Logger logger = LoggerFactory.getLogger(AuthorizationErrorHandler.class);
  private final ObjectMapper objectMapper;

  @Autowired
  public AuthorizationErrorHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException
      accessDeniedException) throws IOException, ServletException {
    String path = request.getRequestURI().substring(request.getContextPath().length());
//    logger.error(
//        format("error: %s path: %s", path, "Access denied"),
//        accessDeniedException
//    );
    response.setContentType(APPLICATION_JSON_VALUE);
    response.setStatus(SC_FORBIDDEN);
    objectMapper.writeValue(response.getOutputStream(), new SecurityErrorDetails(
        UUID.randomUUID(),
        new Date(),
        FORBIDDEN,
        accessDeniedException,
        "Access denied",
        path
    ));
  }
}

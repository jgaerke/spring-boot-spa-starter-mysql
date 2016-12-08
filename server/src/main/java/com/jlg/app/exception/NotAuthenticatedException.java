package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED, reason = "Not authenticated")
public class NotAuthenticatedException extends RuntimeException {
  private static final long serialVersionUID = -4789387106212276067L;
}

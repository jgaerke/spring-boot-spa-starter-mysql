package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(value = UNAUTHORIZED, reason = "Account not found for current user principal")
public class AccountPrincipalMismatchException extends RuntimeException {
  private static final long serialVersionUID = 8354682858842291803L;
}

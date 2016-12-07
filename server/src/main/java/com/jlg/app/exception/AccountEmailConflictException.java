package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT, reason = "Email taken")
public class AccountEmailConflictException extends RuntimeException {
  private static final long serialVersionUID = 4312770097312209420L;
}

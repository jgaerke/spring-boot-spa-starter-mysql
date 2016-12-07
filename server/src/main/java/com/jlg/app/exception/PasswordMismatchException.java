package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST, reason = "Password does not match")
public class PasswordMismatchException extends RuntimeException {
  private static final long serialVersionUID = 3206991102429902146L;
}

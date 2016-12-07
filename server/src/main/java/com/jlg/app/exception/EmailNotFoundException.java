package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = "Email not found")
public class EmailNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -7406630554270011192L;
}

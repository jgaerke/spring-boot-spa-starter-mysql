package com.jlg.app.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = "Password reset token not found")
public class PasswordResetTokenNotFoundException extends RuntimeException {
  private static final long serialVersionUID = -2483888538217709368L;
}

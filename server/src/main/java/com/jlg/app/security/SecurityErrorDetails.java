package com.jlg.app.security;

import java.util.Date;
import java.util.UUID;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
public class SecurityErrorDetails {
  private String correlationId;
  private Date timestamp;
  private int status;
  private String error;
  private String exception;
  private String message;
  private String path;

  public SecurityErrorDetails(
      UUID correlationId,
      Date timestamp,
      HttpStatus status,
      Throwable exception,
      String message,
      String path
  ) {
    this.correlationId = correlationId.toString();
    this.timestamp = timestamp;
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.exception = exception.getClass().getName();
    this.message = message;
    this.path = path;
  }
}

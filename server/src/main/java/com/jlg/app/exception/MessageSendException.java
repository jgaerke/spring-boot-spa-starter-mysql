package com.jlg.app.exception;

import static java.lang.String.format;

public class MessageSendException extends RuntimeException {
  private static final long serialVersionUID = -5691348606685315231L;

  public MessageSendException(int code, String reason) {
    super(format("MailMessage failed to send. Code: %s, Reason: %s", code, reason));
  }

  public MessageSendException(Throwable cause) {
    super(cause);
  }
}

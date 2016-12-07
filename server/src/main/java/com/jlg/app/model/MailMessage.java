package com.jlg.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MailMessage {
  private String from;
  private String to;
  private String subject;
  private String text;
}

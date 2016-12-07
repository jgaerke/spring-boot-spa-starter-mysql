package com.jlg.app.service;

import com.jlg.app.model.MailMessage;
import org.springframework.stereotype.Component;

@Component
public class AwsMailService implements MailService {
  @Override
  public void send(MailMessage message) {

  }
}

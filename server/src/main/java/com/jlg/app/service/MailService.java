package com.jlg.app.service;

import com.jlg.app.model.MailMessage;

public interface MailService {
  void send(MailMessage message);
}

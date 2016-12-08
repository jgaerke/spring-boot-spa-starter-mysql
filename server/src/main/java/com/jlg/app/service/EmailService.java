package com.jlg.app.service;

import com.jlg.app.domain.Message;

public interface EmailService {
  void send(Message message);
}

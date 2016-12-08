package com.jlg.app.service;

import com.jlg.app.domain.Message;
import com.jlg.app.exception.MessageSendException;
import com.sendgrid.*;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static java.util.Optional.ofNullable;

@Service
public class SendGridEmailServiceImpl implements EmailService {

  private final String sendGridApiKey;
  private final String sendGridFromAddress;

  @Autowired
  public SendGridEmailServiceImpl(
      @Value("${sendgrid.api.key}") String sendGridApiKey,
      @Value("${sendgrid.from.address}") String sendGridFromAddress) {
    this.sendGridApiKey = sendGridApiKey;
    this.sendGridFromAddress = sendGridFromAddress;
  }

  @Override
  public void send(Message message) {
    Email from = new Email(ofNullable(message.getFrom()).orElse(sendGridFromAddress));
    Email to = new Email(message.getTo());
    Content content = new Content(message.getContentType(), message.getContent());
    Mail mail = new Mail(from, message.getSubject(), to, content);

    SendGrid sg = new SendGrid(sendGridApiKey);

    Request request = new Request();
    try {
      request.method = Method.POST;
      request.endpoint = "mail/send";
      request.body = mail.build();
      Response response = sg.api(request);
      System.out.println(response.statusCode);
      System.out.println(response.body);
      System.out.println(response.headers);
    } catch (IOException ex) {
      throw new MessageSendException(ex);
    }
  }
}

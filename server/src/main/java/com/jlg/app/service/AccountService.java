package com.jlg.app.service;

import com.jlg.app.exception.AccountEmailConflictException;
import com.jlg.app.exception.AccountPrincipalMismatchException;
import com.jlg.app.exception.EmailNotFoundException;
import com.jlg.app.exception.PasswordResetTokenNotFoundException;
import com.jlg.app.model.Account;
import com.jlg.app.model.MailMessage;
import com.jlg.app.repository.AccountRepository;
import com.jlg.app.request.AccountUpdateRequest;
import com.jlg.app.request.PasswordChangeRequest;
import com.jlg.app.request.PasswordResetRequest;
import com.jlg.app.request.RegistrationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static java.lang.String.format;
import static java.lang.System.getProperty;

@Service
public class AccountService {
  private final UserDetailsService userDetailsService;
  private final AccountRepository accountRepository;
  private PasswordEncoder passwordEncoder;
  private final MailService mailService;
  private final Environment environment;

  @Autowired
  public AccountService(
      UserDetailsService userDetailsService,
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      MailService mailService,
      Environment environment) {
    this.userDetailsService = userDetailsService;
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
    this.mailService = mailService;
    this.environment = environment;
  }

  public Account create(RegistrationRequest registrationRequest) {
    if (accountRepository.findOneByEmail(registrationRequest.getEmail()).isPresent()) {
      throw new AccountEmailConflictException();
    }

    return accountRepository.save(
        registrationRequest.withPassword(
            passwordEncoder.encode(registrationRequest.getPassword())
        ).toAccount()
    );
  }

  public Account update(String existingEmail, AccountUpdateRequest accountUpdateRequest) {
    Optional<Account> existing = accountRepository.findOneByEmail(existingEmail);
    if (!existing.isPresent()) {
      throw new EmailNotFoundException();
    }

    return accountRepository.save(accountUpdateRequest.copyToAccount(existing.get()));
  }

  public Account changePassword(PasswordChangeRequest passwordChangeRequest, String email) {
    Optional<Account> account = accountRepository.findOneByEmail(email);
    if (!account.isPresent()) {
      throw new AccountPrincipalMismatchException();
    }

    return accountRepository.save(
        account.get().withPassword(passwordEncoder.encode(passwordChangeRequest.getPassword()))
    );
  }

  public Account resetPassword(PasswordResetRequest passwordResetRequest) {

    Optional<Account> account =
        accountRepository.findOneByPasswordResetToken(passwordResetRequest.getPasswordResetToken());
    if (!account.isPresent()) {
      throw new PasswordResetTokenNotFoundException();
    }

    return accountRepository.save(
        account.get()
            .withPassword(passwordEncoder.encode(passwordResetRequest.getPassword()))
            .withPasswordResetToken(null)
    );

  }

  public boolean sendPasswordResetInstructions(String email) {
    Optional<Account> account = accountRepository.findOneByEmail(email);
    if (!account.isPresent()) {
      throw new EmailNotFoundException();
    }

    String passwordResetToken = UUID.randomUUID().toString();

    accountRepository.save(account.get().withPasswordResetToken(passwordResetToken));

    mailService.send(new MailMessage(
        "some-email.com", //TODO:: fix this.
        email,
        "Password Reset",
        format(
            "Please click the following link to reset your password: %s" +
                "http://localhost:8080/app/reset-password/%s",
            getProperty("line.separator"),
            passwordResetToken
        )
    ));

    return true;
  }

  public Optional<Account> getByEmail(String email) {
    return accountRepository.findOneByEmail(email);
  }
}

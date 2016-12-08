package com.jlg.app.service;

import com.jlg.app.domain.Account;
import com.jlg.app.domain.Message;
import com.jlg.app.exception.AccountEmailConflictException;
import com.jlg.app.exception.AccountPrincipalMismatchException;
import com.jlg.app.exception.EmailNotFoundException;
import com.jlg.app.exception.PasswordResetTokenNotFoundException;
import com.jlg.app.repository.AccountRepository;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static java.lang.String.format;
import static java.lang.System.getProperty;

@Service
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;
  private final EmailService emailService;
  private PasswordEncoder passwordEncoder;

  @Autowired
  public AccountServiceImpl(
      AccountRepository accountRepository,
      PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.accountRepository = accountRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  @Override
  public Account create(Account account) {
    ifEmailTakenThrowException(account);
    return accountRepository.save(account
        .withPassword(passwordEncoder.encode(account.getPassword())));
  }

  private void ifEmailTakenThrowException(Account account) {
    if (accountRepository.findOneByEmail(account.getEmail()).isPresent()) {
      throw new AccountEmailConflictException();
    }
  }

  @Override
  public Account update(String existingEmail, Account account) {
    Optional<Account> existing = accountRepository.findOneByEmail(existingEmail);
    if (!existing.isPresent()) {
      throw new EmailNotFoundException();
    }
    if (!account.getEmail().equals(existingEmail)) {
      ifEmailTakenThrowException(account);
    }
    return accountRepository.save(existing.get()
        .withEmail(account.getEmail()))
        .withFirst(account.getFirst())
        .withLast(account.getLast());
  }

  @Override
  public Account changePassword(Account passwordChange, String email) {
    Optional<Account> account = accountRepository.findOneByEmail(email);
    if (!account.isPresent()) {
      throw new AccountPrincipalMismatchException();
    }
    return accountRepository.save(account.get().withPassword(passwordEncoder.encode(passwordChange.getPassword())));
  }

  @Override
  public Account resetPassword(Account passwordReset) {
    Optional<Account> account =
        accountRepository.findOneByPasswordResetToken(passwordReset.getPasswordResetToken());
    if (!account.isPresent()) {
      throw new PasswordResetTokenNotFoundException();
    }
    return accountRepository.save(
        account.get()
            .withPassword(passwordEncoder.encode(passwordReset.getPassword()))
            .withPasswordResetToken(null)
    );
  }

  @Override
  public boolean sendPasswordResetInstructions(String email) {
    Optional<Account> account = accountRepository.findOneByEmail(email);
    if (!account.isPresent()) {
      throw new EmailNotFoundException();
    }

    String passwordResetToken = UUID.randomUUID().toString();

    accountRepository.save(account.get().withPasswordResetToken(passwordResetToken));

    emailService.send(Message.builder()
        .to(email)
        .subject("Password Reset")
        .contentType("text/plain")
        .content(format(
            "Please click the following link to reset your password: %s" +
                "http://localhost:8080/app/account/password/reset/%s",
            getProperty("line.separator"),
            passwordResetToken
        )).build()
    );

    return true;
  }

  @Override
  public Optional<Account> getByEmail(String email) {
    return accountRepository.findOneByEmail(email);
  }
}

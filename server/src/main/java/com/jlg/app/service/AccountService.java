package com.jlg.app.service;

import com.jlg.app.domain.Account;
import java.util.Optional;

public interface AccountService {
  Account create(Account account);

  Account update(String existingEmail, Account account);

  Account changePassword(Account passwordChange, String email);

  Account resetPassword(Account passwordReset);

  boolean sendPasswordResetInstructions(String email);

  Optional<Account> getByEmail(String email);
}

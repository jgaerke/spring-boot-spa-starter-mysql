package com.jlg.app;

import com.jlg.app.domain.Account;

import java.util.Optional;
import java.util.UUID;

public class TestUtil {
  public static Account createValidNewAccount() {
    return Account.builder()
        .id(null)
        .email("some-email@gmail.com")
        .password("password")
        .first("jeremy")
        .last("gaerke")
        .credentialsExpired(false)
        .active(true)
        .expired(false)
        .locked(false)
        .passwordResetToken(null)
        .build();
  }

  public static Account createValidExistingAccount() {
    return Account.builder()
        .id(Optional.of(UUID.randomUUID()))
        .email("some-email@gmail.com")
        .password("password")
        .first("jeremy")
        .last("gaerke")
        .credentialsExpired(false)
        .active(true)
        .expired(false)
        .locked(false)
        .passwordResetToken(null)
        .build();
  }


  public static Account createValidRegistrationRequest() {
    return Account.builder().email("some-email@gmail.com").password("some-password").build();
  }
}

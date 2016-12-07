package com.jlg.app;

import com.google.common.collect.Sets;
import com.jlg.app.model.Account;
import com.jlg.app.request.RegistrationRequest;

import java.util.UUID;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

public class TestUtil {
  public static Account createValidNewAccount() {
    return Account.builder()
        .id(null)
        .email("some-email@gmail.com")
        .password("password")
        .roles(newHashSet())
        .first("jeremy")
        .last("gaerke")
        .credentialsExpired(false)
        .disabled(false)
        .expired(false)
        .locked(false)
        .passwordResetToken(null)
        .build();
  }

  public static Account createValidExistingAccount() {
    return Account.builder()
        .id(UUID.randomUUID())
        .email("some-email@gmail.com")
        .password("password")
        .roles(newHashSet())
        .first("jeremy")
        .last("gaerke")
        .credentialsExpired(false)
        .disabled(false)
        .expired(false)
        .locked(false)
        .passwordResetToken(null)
        .build();
  }


  public static RegistrationRequest createValidRegistrationRequest() {
    return new RegistrationRequest("some-email@gmail.com", "some-password");
  }
}

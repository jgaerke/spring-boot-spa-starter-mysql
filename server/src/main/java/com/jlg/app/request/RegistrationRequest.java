package com.jlg.app.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jlg.app.model.Account;
import lombok.Getter;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Getter
@Wither
public class RegistrationRequest {
  @NotEmpty
  @Size(max = 254)
  private final String email;

  @NotEmpty
  @Size(max = 254)
  private final String password;

  @JsonCreator
  public RegistrationRequest(
      @JsonProperty("email") String email,
      @JsonProperty("password") String password) {
    this.email = email;
    this.password = password;
  }

  public Account toAccount() {
    return Account.builder().email(email).password(password).build();
  }
}


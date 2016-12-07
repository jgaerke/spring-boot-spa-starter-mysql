package com.jlg.app.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Getter
public class PasswordRecoveryValidationRequest {
  @NotEmpty
  @Size(max = 254)
  private String token;

  @JsonCreator
  public PasswordRecoveryValidationRequest(
      @JsonProperty("token") String token) {
    this.token = token;
  }

}


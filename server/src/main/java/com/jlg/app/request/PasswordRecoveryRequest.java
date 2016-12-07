package com.jlg.app.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

@Getter
public class PasswordRecoveryRequest {
  @NotEmpty
  @Size(max = 254)
  private String email;

  @JsonCreator
  public PasswordRecoveryRequest(
      @JsonProperty("email") String email) {
    this.email = email;
  }

}


package com.jlg.app.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;


@Getter
public class PasswordChangeRequest {
  @NotEmpty
  @Size(max = 254)
  private String password;

  @JsonCreator
  public PasswordChangeRequest(
      @JsonProperty String password) {
    this.password = password;
  }
}


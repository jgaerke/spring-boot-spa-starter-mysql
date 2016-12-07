package com.jlg.app.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {
  @NotEmpty
  @Size(max = 254)
  private String passwordResetToken;

  @NotEmpty
  @Size(max = 254)
  private String password;
}


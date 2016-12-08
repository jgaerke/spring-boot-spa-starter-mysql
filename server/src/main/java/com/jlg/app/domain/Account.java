package com.jlg.app.domain;

import com.jlg.app.validation.group.AccountCreation;
import com.jlg.app.validation.group.AccountUpdate;
import com.jlg.app.validation.group.PasswordChange;
import com.jlg.app.validation.group.PasswordRecovery;
import com.jlg.app.validation.group.PasswordReset;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.mockito.cglib.core.Local;
import org.springframework.security.core.GrantedAuthority;

import static java.time.temporal.ChronoUnit.SECONDS;

@AllArgsConstructor
@Value
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@Wither
public class Account {

  private Optional<UUID> id;
  @NotEmpty(groups = {AccountCreation.class, AccountUpdate.class, PasswordRecovery.class})
  @Email(groups = {AccountCreation.class, AccountUpdate.class, PasswordRecovery.class})
  private String email;
  @NotEmpty(groups = {AccountCreation.class, PasswordReset.class, PasswordChange.class})
  private String password;
  @Size(max = 255)
  private String first;
  @Size(max = 255)
  private String last;
  @Size(max = 55)
  private String plan;
  private Map<String, Object> paymentInfo;
  private LocalDateTime trialExpirationDate;
  @NotNull(groups = PasswordReset.class)
  private String passwordResetToken;
  private boolean locked;
  private boolean expired;
  private boolean credentialsExpired;
  private boolean active;
  private LocalDateTime created;
  private LocalDateTime modified;
  private List<? extends GrantedAuthority> authorities;

  public LocalDateTime getTrialExpirationDate() {
    return trialExpirationDate.truncatedTo(SECONDS);
  }

  public LocalDateTime getCreated() {
    return created.truncatedTo(SECONDS);
  }

  public LocalDateTime getModified() {
    return modified.truncatedTo(SECONDS);
  }

  public Account() {
    this.id = null;
    this.email = null;
    this.password = null;
    this.first = null;
    this.last = null;
    this.plan = null;
    this.paymentInfo = null;
    this.trialExpirationDate = null;
    this.passwordResetToken = null;
    this.locked = false;
    this.expired = false;
    this.credentialsExpired = false;
    this.active = true;
    this.authorities = null;
    this.created = LocalDateTime.now();
    this.modified = LocalDateTime.now();
  }
}

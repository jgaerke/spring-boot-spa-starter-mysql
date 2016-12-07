package com.jlg.app.model;

import lombok.*;
import lombok.experimental.Wither;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Wither
@Entity
public class Account {
  @Id
  private UUID id;

  private String email;

  @NotEmpty
  @Size(max = 254)
  private String password;

  private String first;

  private String last;

  private String plan;

  private String paymentInfo;

  private Date trialExpirationDate;

  private String passwordResetToken;

  private boolean locked;

  private boolean expired;

  private boolean credentialsExpired;

  private boolean disabled;

  @OneToMany(orphanRemoval = true)
  @JoinColumn(name = "account")
  private Set<Role> roles;

}


package com.jlg.app.model;

import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


@Value
@Entity
public class Role implements GrantedAuthority {

  @Id
  private UUID account;

  @Id
  private String name;

  public Role(String name) {
    this.account = null;
    this.name = name;
  }

  @Override
  public String getAuthority() {
    return name;
  }
}

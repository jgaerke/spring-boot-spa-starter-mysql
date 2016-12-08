package com.jlg.app.repository;

import com.jlg.app.domain.Account;
import com.jlg.app.repository.mapper.AccountRowMapper;
import com.jlg.app.repository.statements.AccountSqlStatements;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import static java.time.ZoneOffset.UTC;
import static java.util.Date.from;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.UUID.randomUUID;

@Repository
public class AccountRepository {

  public static String ID = "id";
  public static String EMAIL = "email";
  public static String PASSWORD = "password";
  public static String PASSWORD_RESET_TOKEN = "password_reset_token";
  public static String FIRST = "first";
  public static String LAST = "last";
  public static String PLAN = "plan";
  public static String TRIAL_EXPIRATION_DATE = "trial_expiration_date";
  public static String LOCKED = "locked";
  public static String DISABLED = "disabled";
  public static String EXPIRED = "expired";
  public static String ACTIVE = "active";
  public static String CREATED = "created";
  public static String MODIFIED = "modified";
  private final JdbcTemplate jdbcTemplate;
  private final AccountRowMapper accountRowMapper;

  @Autowired
  public AccountRepository(
      JdbcTemplate jdbcTemplate,
      AccountRowMapper accountRowMapper) {
    this.jdbcTemplate = jdbcTemplate;
    this.accountRowMapper = accountRowMapper;
  }

  public Optional<Account> findOneByEmail(String email) {
    List<Account> matches = jdbcTemplate.query(
        AccountSqlStatements.selectByEmail(),
        this.accountRowMapper::map,
        email
    );
    return matches.isEmpty() ? empty() : of(matches.get(0));
  }

  public Optional<Account> findOneByPasswordResetToken(String passwordResetToken) {
    List<Account> matches = jdbcTemplate.query(
        AccountSqlStatements.selectByPasswordResetToken(),
        this.accountRowMapper::map,
        passwordResetToken
    );
    return matches.isEmpty() ? empty() : of(matches.get(0));
  }

  public Account save(Account account) {
    if (account.getId().isPresent()) {
      jdbcTemplate.update(
          AccountSqlStatements.update(),
          account.getEmail(),
          account.getPassword(),
          account.getPasswordResetToken(),
          account.getFirst(),
          account.getLast(),
          account.getPlan(),
          from(account.getTrialExpirationDate().toInstant(UTC)),
          account.isLocked(),
          account.isActive(),
          account.isExpired(),
          account.isActive(),
          from(account.getCreated().toInstant(UTC)),
          from(account.getModified().toInstant(UTC)),
          account.getId().get().toString()
      );
    } else {
      UUID id = randomUUID();
      jdbcTemplate.update(
          AccountSqlStatements.insert(),
          id.toString(),
          account.getEmail(),
          account.getPassword(),
          account.getPasswordResetToken(),
          account.getFirst(),
          account.getLast(),
          account.getPlan(),
          from(account.getTrialExpirationDate().toInstant(UTC)),
          account.isLocked(),
          account.isActive(),
          account.isExpired(),
          account.isActive(),
          from(account.getCreated().toInstant(UTC)),
          from(account.getModified().toInstant(UTC))
      );
      account = account.withId(of(id));
    }
    return account;
  }

  public void removeById(UUID id) {
    jdbcTemplate.update(
        AccountSqlStatements.deleteById(),
        id.toString()
    );
  }
}

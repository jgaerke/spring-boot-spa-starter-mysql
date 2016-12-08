package com.jlg.app.repository.mapper;

import com.jlg.app.domain.Account;
import com.jlg.app.repository.table.AccountTable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Component;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.LocalDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.of;

@Component
public class AccountRowMapper {
    public Account map(ResultSet rs, int rowNum) throws SQLException {
        return Account.builder()
                .id(of(UUID.fromString(rs.getString(AccountTable.Column.ID))))
                .email(rs.getString(AccountTable.Column.EMAIL))
                .password(rs.getString(AccountTable.Column.PASSWORD))
                .passwordResetToken(rs.getString(AccountTable.Column.PASSWORD_RESET_TOKEN))
                .first(rs.getString(AccountTable.Column.FIRST))
                .last(rs.getString(AccountTable.Column.LAST))
                .plan(rs.getString(AccountTable.Column.PLAN))
                .trialExpirationDate(ofInstant(new Date(rs.getTimestamp(AccountTable.Column.TRIAL_EXPIRATION_DATE)
                        .getTime()).toInstant(), UTC))
                .locked(rs.getBoolean(AccountTable.Column.LOCKED))
                .active(rs.getBoolean(AccountTable.Column.ACTIVE))
                .expired(rs.getBoolean(AccountTable.Column.EXPIRED))
                .created(ofInstant(new Date(rs.getTimestamp(AccountTable.Column.CREATED).getTime()).toInstant(), UTC))
                .modified(ofInstant(new Date(rs.getTimestamp(AccountTable.Column.MODIFIED).getTime()).toInstant(), UTC))
                .authorities(newArrayList())
                .build();
    }
}

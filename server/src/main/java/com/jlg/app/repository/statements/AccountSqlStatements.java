package com.jlg.app.repository.statements;

import com.jlg.app.repository.table.AccountTable;

import static java.util.stream.IntStream.range;

public class AccountSqlStatements {
  public static String insert() {
    StringBuilder insert = new StringBuilder();
    insert.append("insert into " + AccountTable.NAME + " (");
    insert.append(allFields());
    insert.append(") values (");
    range(0, 13).forEach(i -> insert.append("?,"));
    insert.append(("?)"));
    return insert.toString();
  }

  public static String update() {
    StringBuilder update = new StringBuilder();
    update.append("update " + AccountTable.NAME + " ");
    update.append("set " + AccountTable.Column.EMAIL + " = ?, ");
    update.append(AccountTable.Column.PASSWORD + " = ?, ");
    update.append(AccountTable.Column.PASSWORD_RESET_TOKEN + " = ?, ");
    update.append(AccountTable.Column.FIRST + " = ?, ");
    update.append(AccountTable.Column.LAST + " = ?, ");
    update.append(AccountTable.Column.PLAN + " = ?, ");
    update.append(AccountTable.Column.TRIAL_EXPIRATION_DATE + " = ?, ");
    update.append(AccountTable.Column.LOCKED + " = ?, ");
    update.append(AccountTable.Column.ACTIVE + " = ?, ");
    update.append(AccountTable.Column.EXPIRED + " = ?, ");
    update.append(AccountTable.Column.ACTIVE + " = ?, ");
    update.append(AccountTable.Column.CREATED + " = ?, ");
    update.append(AccountTable.Column.MODIFIED + " = ? ");
    update.append("where " + AccountTable.Column.ID + " = ?");
    return update.toString();
  }

  public static String selectByEmail() {
    StringBuilder query = new StringBuilder();
    query.append(selectAllFrom() + " ");
    query.append("where " + AccountTable.Column.EMAIL + " = ?");
    return query.toString();
  }

  public static String selectByPasswordResetToken() {
    StringBuilder query = new StringBuilder();
    query.append(selectAllFrom() + " ");
    query.append("where " + AccountTable.Column.PASSWORD_RESET_TOKEN + " = ?");
    return query.toString();
  }

  public static String deleteById() {
    StringBuilder delete = new StringBuilder();
    delete.append("delete from " + AccountTable.NAME + " ");
    delete.append("where " + AccountTable.Column.ID + " = ?");
    return delete.toString();
  }

  private static String selectAllFrom() {
    StringBuilder query = new StringBuilder();
    query.append("select ");
    query.append(allFields() + " ");
    query.append("from " + AccountTable.NAME);
    return query.toString();
  }

  private static String allFields() {
    StringBuilder query = new StringBuilder();
    query.append(AccountTable.Column.ID + ", ");
    query.append(AccountTable.Column.EMAIL + ", ");
    query.append(AccountTable.Column.PASSWORD + ", ");
    query.append(AccountTable.Column.PASSWORD_RESET_TOKEN + ", ");
    query.append(AccountTable.Column.FIRST + ", ");
    query.append(AccountTable.Column.LAST + ", ");
    query.append(AccountTable.Column.PLAN + ", ");
    query.append(AccountTable.Column.TRIAL_EXPIRATION_DATE + ", ");
    query.append(AccountTable.Column.LOCKED + ", ");
    query.append(AccountTable.Column.ACTIVE + ", ");
    query.append(AccountTable.Column.EXPIRED + ", ");
    query.append(AccountTable.Column.ACTIVE + ", ");
    query.append(AccountTable.Column.CREATED + ", ");
    query.append(AccountTable.Column.MODIFIED);
    return query.toString();
  }
}

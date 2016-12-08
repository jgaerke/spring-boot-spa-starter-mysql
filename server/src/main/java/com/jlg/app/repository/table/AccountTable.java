package com.jlg.app.repository.table;

public class AccountTable {
    public static String NAME = "account";

    public static class Column {
        public static String ID = "id";
        public static String EMAIL = "email";
        public static String PASSWORD = "password";
        public static String PASSWORD_RESET_TOKEN = "passwordResetToken";
        public static String FIRST = "first";
        public static String LAST = "last";
        public static String PLAN = "plan";
        public static String TRIAL_EXPIRATION_DATE = "trialExpirationDate";
        public static String LOCKED = "locked";
        public static String EXPIRED = "expired";
        public static String ACTIVE = "active";
        public static String CREATED = "created";
        public static String MODIFIED = "modified";
    }
}

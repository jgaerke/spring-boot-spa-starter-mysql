package com.jlg.app.util;

import static com.google.common.base.Strings.isNullOrEmpty;

public class StringUtil {
  public static String nullIfEmpty(String value) {
    if (isNullOrEmpty(value)) {
      return null;
    }
    return value;
  }
}

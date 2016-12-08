package com.jlg.app.util;

import java.time.LocalDateTime;
import java.util.Date;
import org.apache.http.MethodNotSupportedException;

import static java.time.ZoneId.systemDefault;

public class DateUtil {

  private DateUtil() throws MethodNotSupportedException {
    throw new MethodNotSupportedException("Utility classes cannot be instantiated");
  }

  public static LocalDateTime convertToLocalDateTime(Date date) {
    return date.toInstant().atZone(systemDefault()).toLocalDateTime();
  }

  public static Date convertToDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(systemDefault()).toInstant());
  }
}


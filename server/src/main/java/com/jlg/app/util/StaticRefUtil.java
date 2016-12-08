package com.jlg.app.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StaticRefUtil {
  private static Map<Class<?>, Object> staticRefs = new ConcurrentHashMap<>();

  public static void addRef(Class<?> clazz, Object ref) {
    staticRefs.put(clazz, ref);
  }

  public static <T> T getRef(Class<T> clazz) {
    return (T) staticRefs.get(clazz);
  }
}

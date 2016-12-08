package com.jlg.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapUtil {
  public static Map pruneNullAndEmptyEntries(Map map) {
    if (map == null) {
      return null;
    }
    ImmutableMap.Builder builder = ImmutableMap.builder();
    map.forEach((k, v) -> {
      if (v != null) {
        if (v instanceof Map) {
          if (!((Map) v).isEmpty()) {
            builder.put(k, pruneNullAndEmptyEntries((Map) v));
          }
        } else if (v instanceof List) {
          if (!((List) v).isEmpty()) {
            builder.put(k, v);
          }
        } else {
          builder.put(k, v);
        }
      }
    });
    return builder.build();
  }

  public static Map<String, Object> fromString(String data) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.readValue(data, Map.class);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toString(Map data) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      return objectMapper.writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}

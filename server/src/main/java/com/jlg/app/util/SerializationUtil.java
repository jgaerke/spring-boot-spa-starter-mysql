package com.jlg.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.apache.http.MethodNotSupportedException;

public class SerializationUtil {

  private SerializationUtil() throws MethodNotSupportedException {
    throw new MethodNotSupportedException("Utility classes cannot be instantiated");
  }

  public static <T> String serialize(ObjectMapper objectMapper, T input) {
    try {
      return objectMapper.writeValueAsString(input);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unchecked")
  public static <T> T deserialize(ObjectMapper objectMapper, String data, Class<T> valueType) {
    try {
      return objectMapper.readValue(data, valueType);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

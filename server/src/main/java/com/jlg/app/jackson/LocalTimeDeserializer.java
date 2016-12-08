package com.jlg.app.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalTime;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
  @Override
  public LocalTime deserialize(JsonParser p, DeserializationContext ctxt)
      throws IOException, JsonProcessingException {
    return LocalTime.parse(p.getText());
  }
}

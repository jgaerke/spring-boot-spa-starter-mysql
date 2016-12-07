package com.jlg.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xebia.jacksonlombok.JacksonLombokAnnotationIntrospector;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

@Configuration
public class RestConfig extends RepositoryRestConfigurerAdapter {
  @Override
  public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
    objectMapper.setAnnotationIntrospector(new JacksonLombokAnnotationIntrospector());
  }
}

package com.jlg.app.integration.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.Map;

public class MockPostProcessor implements BeanFactoryPostProcessor {
  private Map<String, Object> mocks;

  public MockPostProcessor(Map<String, Object> mocks) {
    this.mocks = mocks;
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    mocks.keySet().stream().forEach(k -> {
      beanFactory.registerSingleton(k, mocks.get(k));
    });
  }
}

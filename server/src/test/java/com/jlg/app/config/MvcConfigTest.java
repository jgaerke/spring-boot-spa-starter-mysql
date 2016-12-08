package com.jlg.app.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MvcConfigTest {
  @Test
  public void should_add_view_controllers() throws Exception {
    //given
    MvcConfig mvcConfig = new MvcConfig();
    ViewControllerRegistry registry = mock(ViewControllerRegistry.class);
    ViewControllerRegistration registration = mock(ViewControllerRegistration.class);
    when(registry.addViewController(anyString())).thenReturn(registration);

    //when
    mvcConfig.addViewControllers(registry);

    //then
    verify(registry).addViewController(eq("/app"));
    verify(registry).addViewController(eq("/app/**"));
    verify(registration, times(2)).setViewName(eq("app"));
    verify(registry).addViewController(eq("/"));
    verify(registration).setViewName(eq("home"));
  }
}
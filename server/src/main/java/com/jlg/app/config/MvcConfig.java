package com.jlg.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
import org.springframework.web.servlet.resource.VersionResourceResolver;

import static java.util.Arrays.asList;

@Configuration
@EnableJdbcHttpSession
public class MvcConfig extends WebMvcConfigurerAdapter {

  @Autowired
  Environment environment;

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    boolean production = asList(environment.getActiveProfiles()).contains("production");
    String resouceLocation = production ? "classpath:static/" : "file:src/main/resources/static/";
    registry.addResourceHandler("/**")
        .addResourceLocations(resouceLocation)
        .resourceChain(true)
        .addResolver(
            new VersionResourceResolver()
//                .addFixedVersionStrategy("dev", "/**/*.js")
                .addContentVersionStrategy("/**")
        )
        .addTransformer(new AppCacheManifestTransformer());
  }

  @Override
  public void addViewControllers(ViewControllerRegistry registry) {
    super.addViewControllers(registry);
    registry.addViewController("/app").setViewName("app");
    registry.addViewController("/app/**").setViewName("app");
    registry.addViewController("/").setViewName("home");
  }

  @Bean
  public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
    return new ResourceUrlEncodingFilter();
  }
}

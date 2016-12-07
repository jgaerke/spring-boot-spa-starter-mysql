package com.jlg.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.resource.ResourceUrlProvider;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class PartialsController {
  @Autowired
  ResourceUrlProvider resourceUrlProvider;

  @RequestMapping(method = GET, path = "/partials")
  public Map<String, String> list() {
    HashMap<String, String> data = new HashMap<>();
    data.put("foo", resourceUrlProvider.getForLookupPath("/partials/bar.html"));
    return data;
  }
}

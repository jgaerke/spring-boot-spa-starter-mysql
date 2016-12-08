package com.jlg.app.functional;


import com.jlg.app.Application;
import com.jlg.app.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.nio.file.Path;

import static java.lang.Runtime.getRuntime;
import static java.nio.file.Paths.get;
import static org.junit.Assert.fail;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = DEFINED_PORT)
public class FunctionalTest {

  @Autowired
  AccountRepository accountRepository;

  @Value("${local.server.port}")
  private int port;

  @Test
  public void should_pass_client_functional_tests() throws IOException {
    try {
      String line;
      Process p = getRuntime().exec(getClientDirectory());
      BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      while ((line = input.readLine()) != null) {
        System.out.println(line);
      }
      input.close();
      int code = p.waitFor();
      if (code != 0) {
        fail("Non-zero exit code encountered while running functional tests");
      }
    } catch (Exception err) {
      err.printStackTrace();
      fail();
    }
  }

  private String getClientDirectory() throws URISyntaxException {
    Path path = get(FunctionalTest.class.getResource(".").toURI());
    while (!path.endsWith("spring-boot-spa-starter-mysql")) {
      path = path.getParent();
    }
    return path.toString() + "/etc/scripts/run-func-tests.sh";
  }
}

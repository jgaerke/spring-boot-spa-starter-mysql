package com.jlg.app.controller;

import com.jlg.app.domain.Account;
import com.jlg.app.exception.EmailNotFoundException;
import com.jlg.app.exception.NotAuthenticatedException;
import com.jlg.app.service.AccountService;
import com.jlg.app.service.AccountServiceImpl;
import com.jlg.app.validation.group.*;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.util.Optional.ofNullable;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping(value = "/api/accounts")
public class AccountController {

  private final AccountService accountService;
  private final UserDetailsService userDetailsService;

  @Autowired
  public AccountController(AccountServiceImpl accountService, UserDetailsService userDetailsService) {
    this.accountService = accountService;
    this.userDetailsService = userDetailsService;
  }

  @RequestMapping(method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
  @ResponseStatus(OK)
  public Account create(@RequestBody @Validated(AccountCreation.class) Account account) {
    Account created = accountService.create(account);
    setAuthToken(created);
    return created;
  }

  @RequestMapping(value = "/password/change", method = PATCH, consumes = APPLICATION_JSON_VALUE, produces =
      APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public void changePassword(
      @RequestBody @Validated(PasswordChange.class) Account passwordChange, HttpServletRequest request) {
    accountService.changePassword(passwordChange, request.getUserPrincipal().getName());
  }

  @RequestMapping(value = "/password/reset", method = POST, consumes = APPLICATION_JSON_VALUE, produces =
      APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public void resetPassword(@RequestBody @Validated(PasswordReset.class) Account passwordReset) {
    accountService.resetPassword(passwordReset);
  }

  @RequestMapping(value = "/password/recover", method = POST, consumes = APPLICATION_JSON_VALUE, produces =
      APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public void recoverPassword(@RequestBody @Validated(PasswordRecovery.class) Account passwordRecovery) {
    accountService.sendPasswordResetInstructions(passwordRecovery.getEmail());
  }

  @RequestMapping(value = "/current", method = GET, produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public Account getCurrent(HttpServletRequest request) {
    String email = ofNullable(request.getUserPrincipal()).orElseThrow(NotAuthenticatedException::new).getName();
    Optional<Account> current = accountService.getByEmail(email);
    if (!current.isPresent()) {
      throw new EmailNotFoundException();
    }
    return current.get();
  }

  @RequestMapping(method = {PATCH, PUT}, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
  @ResponseStatus(OK)
  public void update(@RequestBody @Validated(AccountUpdate.class) Account account, HttpServletRequest request) {
    Account updated = accountService.update(request.getUserPrincipal().getName(), account);
    setAuthToken(updated);
  }

  private void setAuthToken(Account account) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(account.getEmail());
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(userDetails, account.getPassword(), userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}

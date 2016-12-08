package com.jlg.app.security;

import com.jlg.app.domain.Account;
import com.jlg.app.repository.AccountRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.of;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserDetailsServiceImplTest {

  @Test
  public void should_load_user_by_name() throws Exception {
    //given
    String email = "some-email@gmail.com";
    String password = "password";
    String role = "role";
    Account account = Account.builder()
        .email(email)
        .password(password)
        .authorities(newArrayList(new SimpleGrantedAuthority("role")))
        .build();
    AccountRepository mockAccountRepository = mock(AccountRepository.class);
    when(mockAccountRepository.findOneByEmail(eq(email))).thenReturn(of(account));
    UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(mockAccountRepository);

    //when
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

    //then
    assertEquals("User name should be email value supplied", email, userDetails.getUsername());
    assertEquals("Password should be value supplied", password, userDetails.getPassword());
    assertEquals("Role should be value supplied", role, newArrayList(userDetails.getAuthorities()).get(0)
        .getAuthority());
    verify(mockAccountRepository).findOneByEmail(eq(email));
  }

  @Test(expected = UsernameNotFoundException.class)
  public void should_throw_expected_exception_when_user_not_found() throws Exception {
    //given
    AccountRepository mockAccountRepository = mock(AccountRepository.class);
    when(mockAccountRepository.findOneByEmail(anyString())).thenReturn(Optional.empty());
    UserDetailsServiceImpl userDetailsService = new UserDetailsServiceImpl(mockAccountRepository);

    //when
    userDetailsService.loadUserByUsername("some-email@gmail.com");
  }
}
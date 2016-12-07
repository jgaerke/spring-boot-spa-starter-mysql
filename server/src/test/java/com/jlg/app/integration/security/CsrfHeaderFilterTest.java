package com.jlg.app.integration.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CsrfHeaderFilterTest {

  @Test
  public void should_not_do_anything_when_csrf_token_not_set() throws Exception {
    //given
    CsrfHeaderFilter csrfHeaderFilter = new CsrfHeaderFilter();
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    //when
    csrfHeaderFilter.doFilterInternal(request, response, filterChain);

    //then
    verify(request, times(0)).getCookies();
  }

  @Test
  public void should_set_new_response_cookie_when_not_yet_set() throws Exception {
    //given
    CsrfHeaderFilter csrfHeaderFilter = new CsrfHeaderFilter();
    CsrfToken csrfToken = mock(CsrfToken.class);
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getAttribute(eq(CsrfToken.class.getName()))).thenReturn(csrfToken);
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    //when
    csrfHeaderFilter.doFilterInternal(request, response, filterChain);

    //then
    ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
    verify(response).addCookie(argument.capture());
    assertEquals("Should have expected name", "XSRF-TOKEN", argument.getValue().getName());
    assertEquals("Should have expected path", "/", argument.getValue().getPath());
  }

  @Test
  public void should_not_set_new_response_cookie_when_token_value_has_not_changed() throws Exception {
    //given
    CsrfHeaderFilter csrfHeaderFilter = new CsrfHeaderFilter();
    CsrfToken csrfToken = mock(CsrfToken.class);
    when(csrfToken.getToken()).thenReturn("123");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getAttribute(eq(CsrfToken.class.getName()))).thenReturn(csrfToken);
    Cookie cookie = new Cookie("XSRF-TOKEN", "123");
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    //when
    csrfHeaderFilter.doFilterInternal(request, response, filterChain);

    //then
    verify(response, times(0)).addCookie(any(Cookie.class));
  }

  @Test
  public void should_set_new_response_cookie_when_token_value_has_changed() throws Exception {
    //given
    CsrfHeaderFilter csrfHeaderFilter = new CsrfHeaderFilter();
    CsrfToken csrfToken = mock(CsrfToken.class);
    when(csrfToken.getToken()).thenReturn("124");
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getAttribute(eq(CsrfToken.class.getName()))).thenReturn(csrfToken);
    Cookie cookie = new Cookie("XSRF-TOKEN", "123");
    when(request.getCookies()).thenReturn(new Cookie[]{cookie});
    HttpServletResponse response = mock(HttpServletResponse.class);
    FilterChain filterChain = mock(FilterChain.class);

    //when
    csrfHeaderFilter.doFilterInternal(request, response, filterChain);

    //then
    ArgumentCaptor<Cookie> argument = ArgumentCaptor.forClass(Cookie.class);
    verify(response).addCookie(argument.capture());
    assertEquals("Should have expected name", "XSRF-TOKEN", argument.getValue().getName());
    assertEquals("Should have expected path", "/", argument.getValue().getPath());
    assertEquals("Should have expected path", "124", argument.getValue().getValue());
  }
}
package com.example.intentbi.web.authentication;

import com.example.intentbi.configuration.Constants;
import com.example.intentbi.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

  private final String BEARER_AUTH_PREFIX = "Bearer";
  private final JwtTokenService jwtTokenService;
  private final HttpStatusEntryPoint unauthorizedEntryPoint;
  private final List<RequestMatcher> shouldNotFilterRequests =
      List.of(
          new AntPathRequestMatcher(
              Constants.API_URI_PREFIX + "/auth/login", HttpMethod.POST.name()),
          new AntPathRequestMatcher(Constants.API_URI_PREFIX + "/user", HttpMethod.POST.name()));

  @Autowired
  public JwtTokenFilter(
      JwtTokenService jwtTokenService, HttpStatusEntryPoint unauthorizedEntryPoint) {
    this.jwtTokenService = jwtTokenService;
    this.unauthorizedEntryPoint = unauthorizedEntryPoint;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

    if (authHeader == null) {
      throw new IllegalArgumentException(HttpHeaders.AUTHORIZATION + " header was not found");
    } else if (!authHeader.startsWith(BEARER_AUTH_PREFIX)) {
      throw new IllegalArgumentException(
          HttpHeaders.AUTHORIZATION + " header should be prefixed with " + BEARER_AUTH_PREFIX);
    }

    String jwt = authHeader.substring(BEARER_AUTH_PREFIX.length() + 1);
    UserDetails user;

    try {
      user = jwtTokenService.extractUserDetails(jwt);
    } catch (RuntimeException e) {
      AuthenticationException exception = new BadCredentialsException(e.getMessage(), e);
      unauthorizedEntryPoint.commence(request, response, exception);
      return;
    }

    String username = user.getUsername();
    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
    Authentication authentication =
        UsernamePasswordAuthenticationToken.authenticated(username, jwt, authorities);
    SecurityContext context = SecurityContextHolder.createEmptyContext();

    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    request.setAttribute(Constants.JWT_TOKEN_REQUEST_ATTRIBUTE, jwt);

    filterChain.doFilter(request, response);
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    for (RequestMatcher matcher : shouldNotFilterRequests) {
      if (matcher.matches(request)) {
        return true;
      }
    }

    return false;
  }
}

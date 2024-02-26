package com.example.intentbi.web.authentication;

import com.example.intentbi.configuration.Constants;
import com.example.intentbi.service.JwtTokenService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenLogoutSuccessHandler implements LogoutSuccessHandler {

  private final JwtTokenService jwtTokenService;

  @Autowired
  public JwtTokenLogoutSuccessHandler(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {
    String token = (String) request.getAttribute(Constants.JWT_TOKEN_REQUEST_ATTRIBUTE);
    jwtTokenService.blackListJwtToken(token);
  }
}

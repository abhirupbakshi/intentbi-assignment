package com.example.intentbi.web.controller;

import com.example.intentbi.configuration.Constants;
import com.example.intentbi.model.Authority;
import com.example.intentbi.model.User;
import com.example.intentbi.service.JwtTokenService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constants.API_URI_PREFIX + "/auth")
public class AuthController {

  private final JwtTokenService jwtTokenService;

  @Autowired
  public AuthController(JwtTokenService jwtTokenService) {
    this.jwtTokenService = jwtTokenService;
  }

  @PostMapping(path = "/login")
  public ResponseEntity<Map<String, String>> postLogin(Authentication authentication) {
    String username = authentication.getName();
    List<Authority> authorities;

    authorities =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .map(Authority::fromStr)
            .toList();

    User user = new User().setUsername(username).setPassword(null).setAuthorities(authorities);
    String jwt = jwtTokenService.createJwtToken(user);

    return ResponseEntity.accepted()
        .header("Access-Control-Expose-Headers", "*")
        .header(Constants.JWT_TOKEN_RESPONSE_HEADER_ATTRIBUTE, jwt)
        .body(Map.of(Constants.JWT_TOKEN_RESPONSE_HEADER_ATTRIBUTE, jwt));
  }
}

package com.example.intentbi.web.controller;

import com.example.intentbi.configuration.Constants;
import com.example.intentbi.exception.UserAbsentException;
import com.example.intentbi.model.User;
import com.example.intentbi.service.JwtTokenService;
import com.example.intentbi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.net.URI;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.API_URI_PREFIX + "/user")
public class UserController {

  private final JwtTokenService jwtTokenService;
  private final UserService userService;

  @Autowired
  public UserController(JwtTokenService jwtTokenService, UserService userService) {
    this.jwtTokenService = jwtTokenService;
    this.userService = userService;
  }

  @GetMapping
  public ResponseEntity<User> find(Principal principal) {
    String username = principal.getName();
    User user = userService.find(username).orElseThrow(() -> new UserAbsentException(username));

    return ResponseEntity.ok(user);
  }

  @PostMapping
  public ResponseEntity<User> createUser(
      @Valid @RequestBody User user, HttpServletRequest request) {
    user = userService.create(user);
    return ResponseEntity.created(URI.create(request.getRequestURI())).body(user);
  }

  @DeleteMapping
  public ResponseEntity<User> delete(Principal principal, HttpServletRequest request) {
    User user = userService.delete(principal.getName());
    String token = (String) request.getAttribute(Constants.JWT_TOKEN_REQUEST_ATTRIBUTE);

    jwtTokenService.blackListJwtToken(token);

    return ResponseEntity.ok(user);
  }
}

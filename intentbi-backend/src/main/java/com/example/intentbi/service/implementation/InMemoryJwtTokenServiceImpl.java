package com.example.intentbi.service.implementation;

import com.example.intentbi.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class InMemoryJwtTokenServiceImpl implements JwtTokenService {

  private final String ROLE_SEPARATOR = ":";
  private final String ROLES_FIELD_NAME = "authorities";
  private final String ACCOUNT_EXPIRED_FIELD_NAME = "account_expired";
  private final String ACCOUNT_LOCKED_FIELD_NAME = "account_locked";
  private final String CREDENTIAL_EXPIRED_FIELD_NAME = "credentials_expired";
  private final long JWT_EXPIRATION_IN_SECONDS;
  private final Key secretKey;
  private final Set<String> blacklisted = new HashSet<>();

  @Autowired
  public InMemoryJwtTokenServiceImpl(
      @Value("${app.jwt.expiration_seconds}") long JWT_EXPIRATION_IN_SECONDS,
      @Qualifier("secretKey") Key secretKey) {
    this.JWT_EXPIRATION_IN_SECONDS = JWT_EXPIRATION_IN_SECONDS;
    this.secretKey = secretKey;
  }

  @Override
  public boolean isJwtTokenBlacklisted(String jwt) {
    return blacklisted.contains(jwt);
  }

  @Override
  public String createJwtToken(UserDetails userDetails) {
    Objects.requireNonNull(userDetails);

    Collection<? extends GrantedAuthority> authorities1 = userDetails.getAuthorities();
    String authorities =
        authorities1 == null
            ? null
            : authorities1.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(ROLE_SEPARATOR));

    return Jwts.builder()
        .subject(userDetails.getUsername())
        .claim(ROLES_FIELD_NAME, authorities)
        .claim(ACCOUNT_EXPIRED_FIELD_NAME, String.valueOf(!userDetails.isAccountNonExpired()))
        .claim(ACCOUNT_LOCKED_FIELD_NAME, String.valueOf(!userDetails.isAccountNonLocked()))
        .claim(
            CREDENTIAL_EXPIRED_FIELD_NAME, String.valueOf(!userDetails.isCredentialsNonExpired()))
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_IN_SECONDS * 1000))
        .signWith(secretKey)
        .compact();
  }

  @Override
  public void blackListJwtToken(String jwt) {
    blacklisted.add(Objects.requireNonNull(jwt));
  }

  @Override
  public UserDetails extractUserDetails(String jwt) {
    Objects.requireNonNull(jwt);

    if (isJwtTokenBlacklisted(jwt)) {
      throw new JwtException("Jwt token is invalid");
    }

    Jws<Claims> jws = Jwts.parser().setSigningKey(secretKey).build().parseSignedClaims(jwt);

    Claims payload = jws.getPayload();
    List<String> roles =
        Arrays.stream(payload.get(ROLES_FIELD_NAME, String.class).split(ROLE_SEPARATOR)).toList();

    return User.builder()
        .password(jwt)
        .username(payload.getSubject())
        .roles(roles.toArray(new String[0]))
        .accountExpired(Boolean.parseBoolean(payload.get(ACCOUNT_EXPIRED_FIELD_NAME, String.class)))
        .accountLocked(Boolean.parseBoolean(payload.get(ACCOUNT_LOCKED_FIELD_NAME, String.class)))
        .credentialsExpired(
            Boolean.parseBoolean(payload.get(CREDENTIAL_EXPIRED_FIELD_NAME, String.class)))
        .build();
  }
}

package com.example.intentbi.configuration;

import com.example.intentbi.service.UserService;
import com.example.intentbi.web.authentication.JwtTokenFilter;
import com.example.intentbi.web.authentication.JwtTokenLogoutSuccessHandler;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  protected Key secretKey(@Value("${app.jwt.secret_key}") String rawSecretKey) {
    return Keys.hmacShaKeyFor(rawSecretKey.getBytes());
  }

  @Bean
  public HttpStatusEntryPoint unauthorizedEntryPoint() {
    return new HttpStatusEntryPoint(HttpStatus.FORBIDDEN);
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource(
      @Value("${app.cors.allowed_origins}") String allowedOrigins,
      @Value("${app.cors.allowed_methods}") String allowedMethods,
      @Value("${app.cors.allowed_headers}") String allowedHeaders) {
    CorsConfiguration configuration = new CorsConfiguration();
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    for (String origin : allowedOrigins.split(",")) {
      configuration.addAllowedOrigin(origin);
    }
    for (String origin : allowedMethods.split(",")) {
      configuration.addAllowedMethod(origin);
    }
    for (String origin : allowedHeaders.split(",")) {
      configuration.addAllowedHeader(origin);
    }

    configuration.setAllowCredentials(true);
    configuration.addExposedHeader("*");
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }

  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      JwtTokenFilter tokenFilter,
      JwtTokenLogoutSuccessHandler logoutSuccessHandler,
      CorsConfigurationSource corsConfigurationSource)
      throws Exception {
    return http.authorizeHttpRequests(
            req ->
                req.requestMatchers(
                        Constants.API_URI_PREFIX + "/user",
                        Constants.API_URI_PREFIX + "/auth/login")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(tokenFilter, LogoutFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(basic -> basic.authenticationEntryPoint(unauthorizedEntryPoint()))
        .logout(
            logout ->
                logout
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .logoutUrl(Constants.API_URI_PREFIX + "/auth/logout"))
        .csrf(AbstractHttpConfigurer::disable)
        .cors(cors -> cors.configurationSource(corsConfigurationSource))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  public UserDetailsService userDetailsService(UserService userService) {
    return username ->
        userService
            .find(username)
            .orElseThrow(
                () ->
                    new UsernameNotFoundException(
                        "User with username " + username + " does not exist"));
  }
}

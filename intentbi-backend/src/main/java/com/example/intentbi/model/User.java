package com.example.intentbi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor(force = true)
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user_table")
public class User implements UserDetails {

  @JsonProperty("username")
  @NotBlank(message = "username is empty")
  @Id
  @Column(name = "username")
  private String username;

  @JsonProperty(value = "password", access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank(message = "password is blank")
  @Column(name = "password")
  private String password;

  @JsonIgnore
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_authorities", joinColumns = @JoinColumn(name = "username"))
  private List<Authority> authorities;

  @JsonIgnore
  @Column(name = "account_non_expired")
  private boolean accountNonExpired;

  @JsonIgnore
  @Column(name = "account_non_locked")
  private boolean accountNonLocked;

  @JsonIgnore
  @Column(name = "credentials_non_expired")
  private boolean credentialsNonExpired;

  @JsonIgnore
  @Column(name = "enabled")
  private boolean enabled;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (authorities == null) {
      throw new RuntimeException("User roles are null. Cannot create List of GrantedAuthority");
    }

    return authorities.stream().map(a -> new SimpleGrantedAuthority(a.name())).toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return accountNonExpired;
  }

  @Override
  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return credentialsNonExpired;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }
}

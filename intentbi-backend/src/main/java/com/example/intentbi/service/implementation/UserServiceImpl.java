package com.example.intentbi.service.implementation;

import com.example.intentbi.exception.UserAbsentException;
import com.example.intentbi.exception.UserExistsException;
import com.example.intentbi.model.Authority;
import com.example.intentbi.model.User;
import com.example.intentbi.repository.UserRepository;
import com.example.intentbi.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Autowired
  public UserServiceImpl(PasswordEncoder passwordEncoder, UserRepository userRepository) {
    this.passwordEncoder = passwordEncoder;
    this.userRepository = userRepository;
  }

  @Override
  public boolean isUserPresent(String username) {
    return userRepository.existsById(Objects.requireNonNull(username));
  }

  @Override
  public Optional<User> find(String username) {
    return userRepository.findById(Objects.requireNonNull(username));
  }

  @Override
  public User create(User user) {
    Objects.requireNonNull(user);
    Objects.requireNonNull(user.getUsername());

    if (userRepository.existsById(user.getUsername())) {
      throw new UserExistsException(user.getUsername());
    }

    user.setAccountNonExpired(true)
        .setAccountNonLocked(true)
        .setCredentialsNonExpired(true)
        .setEnabled(true)
        .setPassword(passwordEncoder.encode(user.getPassword()))
        .setAuthorities(List.of(Authority.USER));

    return userRepository.save(user);
  }

  @Override
  public User delete(String username) {
    Objects.requireNonNull(username);
    User user =
        userRepository.findById(username).orElseThrow(() -> new UserAbsentException(username));

    userRepository.deleteById(username);

    return user;
  }
}

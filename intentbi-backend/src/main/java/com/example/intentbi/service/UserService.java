package com.example.intentbi.service;

import com.example.intentbi.model.User;
import java.util.Optional;

public interface UserService {

  Optional<User> find(String username);

  boolean isUserPresent(String username);

  User create(User user);

  User delete(String username);
}

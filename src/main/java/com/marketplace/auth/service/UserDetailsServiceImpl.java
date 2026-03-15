package com.marketplace.auth.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.marketplace.auth.entity.User;
import com.marketplace.auth.entity.constant.EntityType;
import com.marketplace.auth.entity.impl.UserDetailsImpl;
import com.marketplace.auth.exception.EntityNotFoundException;
import com.marketplace.auth.repository.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(
        () -> new EntityNotFoundException(EntityType.USER, "name", username));

    return UserDetailsImpl.fromUser(user);
  }

  public UserDetails loadUserById(Long id) {
    User user = userRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(EntityType.USER, "id", id));

    return UserDetailsImpl.fromUser(user);
  }

}

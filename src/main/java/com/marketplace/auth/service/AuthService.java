package com.marketplace.auth.service;

import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.marketplace.auth.entity.JwtPayload;
import com.marketplace.auth.entity.Role;
import com.marketplace.auth.entity.Token;
import com.marketplace.auth.entity.User;
import com.marketplace.auth.entity.constant.EntityType;
import com.marketplace.auth.entity.impl.UserDetailsImpl;
import com.marketplace.auth.entity.request.LoginRequest;
import com.marketplace.auth.entity.request.RegisterRequest;
import com.marketplace.auth.entity.response.AuthenticationResponse;
import com.marketplace.auth.entity.response.UserResponse;
import com.marketplace.auth.exception.EntityNotFoundException;
import com.marketplace.auth.exception.RegisterException;
import com.marketplace.auth.mapper.UserMapper;
import com.marketplace.auth.repository.RoleRepository;
import com.marketplace.auth.repository.TokenRepository;
import com.marketplace.auth.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class AuthService {

  private static final String ROLE_USER = "ROLE_USER";

  private final TokenRepository tokenRepository;

  private final UserRepository userRepository;

  private final RoleRepository roleRepository;

  private final AuthenticationManager authenticationManager;

  private final JwtService jwtService;

  private final PasswordEncoder passwordEncoder;

  private final UserMapper userMapper;

  @Transactional
  public UserResponse register(RegisterRequest registerRequest) {

    Role role = roleRepository.findByName(ROLE_USER).orElseThrow(
        () -> new EntityNotFoundException(EntityType.ROLE, "name", ROLE_USER));

    User newUser = new User(
        registerRequest.getUsername(),
        registerRequest.getEmail(),
        passwordEncoder.encode(registerRequest.getPassword()),
        Set.of(role));

    if (userRepository.existsByUsername(registerRequest.getUsername())) {
      throw new RegisterException("Username already exists");
    }

    if (userRepository.existsByEmail(registerRequest.getEmail())) {
      throw new RegisterException("This email already in use");
    }

    userRepository.save(newUser);

    return userMapper.toUserResponse(newUser);
  }

  @Transactional
  public AuthenticationResponse login(LoginRequest loginRequest) {
    Authentication auth = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();

    User user = userRepository.findById(userDetails.getId()).orElseThrow(
        () -> new EntityNotFoundException(EntityType.USER, "id", userDetails.getId()));

    String accessToken = jwtService.generateAccessToken(userDetails);
    String refreshToken = jwtService.generateRefreshToken(userDetails);

    Token token = new Token(refreshToken, user);
    tokenRepository.save(token);

    return new AuthenticationResponse(accessToken, refreshToken);
  }

  @Transactional
  public AuthenticationResponse refreshToken(String incomingRefreshToken) {
    JwtPayload payload = jwtService.parse(incomingRefreshToken);

    jwtService.validateRefreshToken(payload);

    User user = userRepository.findById(payload.getId())
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.USER, "id", payload.getId()));

    Token existingToken = tokenRepository.findByRefreshToken(incomingRefreshToken)
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.REFRESH_TOKEN, "refresh token", incomingRefreshToken));

    if (!existingToken.getUser().getId().equals(user.getId())) {
      throw new RuntimeException("Something went wrong with refreshToken and user(id not matched)");
    }

    UserDetailsImpl userDetails = UserDetailsImpl.fromUser(user);

    String newAccessToken = jwtService.generateAccessToken(userDetails);
    String newRefreshToken = jwtService.generateRefreshToken(userDetails);

    tokenRepository.deleteByRefreshToken(incomingRefreshToken);
    tokenRepository.save(new Token(newRefreshToken, user));

    return new AuthenticationResponse(newAccessToken, newRefreshToken);
  }

  @Transactional
  public void addRole(String username, String roleName) {
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.ROLE, "name", roleName));
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.USER, "name", username));
    user.getRoles().add(role);
    userRepository.save(user);
  }

  @Transactional
  public void removeRole(String username, String roleName) {
    Role role = roleRepository.findByName(roleName)
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.ROLE, "name", roleName));
    User user = userRepository.findByUsername(username)
        .orElseThrow(
            () -> new EntityNotFoundException(EntityType.USER, "name", username));
    user.getRoles().add(role);
    userRepository.save(user);
  }
}

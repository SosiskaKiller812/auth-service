package com.marketplace.auth.entity.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 4, max = 50, message = "Username should be between 4 and 50 symbols")
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 5, max = 50, message = "Password should be between 5 and 50 symbols")
  private String password;
}

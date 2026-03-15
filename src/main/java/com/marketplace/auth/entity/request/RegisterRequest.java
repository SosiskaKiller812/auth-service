package com.marketplace.auth.entity.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "Username is required")
  @Size(min = 4, max = 50, message = "Username should be between 4 and 50 symbols")
  private String username;

  @Email(message = "Need fill email")
  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  @Size(min = 5, max = 50, message = "Password should be between 5 and 50 symbols")
  private String password;
}

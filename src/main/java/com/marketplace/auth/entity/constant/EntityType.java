package com.marketplace.auth.entity.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EntityType {
  USER("User"),
  REFRESH_TOKEN("Refresh token"),
  ROLE("Role"),
  ACCESS_TOKEN("Access token");

  private final String name;
}

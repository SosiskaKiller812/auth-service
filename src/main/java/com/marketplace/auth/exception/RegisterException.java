package com.marketplace.auth.exception;

public class RegisterException extends RuntimeException {
  public RegisterException(long id) {
    super("Entity with id: " + id + " not found");
  }

  public RegisterException(String name) {
    super("Entity with name: " + name + " not found");
  }
}

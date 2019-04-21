package com.faforever.client.login;

public class LoginFailedException extends RuntimeException {

  public LoginFailedException(String message, Throwable cause) {
    super(message, cause);
  }

  public LoginFailedException(String message) {
    super(message);
  }

  public LoginFailedException(Throwable throwable) {
    super(throwable);
  }
}

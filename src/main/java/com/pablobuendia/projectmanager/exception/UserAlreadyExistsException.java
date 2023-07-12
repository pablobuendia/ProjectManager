package com.pablobuendia.projectmanager.exception;

public class UserAlreadyExistsException extends RuntimeException {

  public UserAlreadyExistsException() {
    super("User already exists");
  }
}

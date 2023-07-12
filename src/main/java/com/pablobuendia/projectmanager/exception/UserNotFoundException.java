package com.pablobuendia.projectmanager.exception;

public class UserNotFoundException extends ResourceNotFoundException {

  public UserNotFoundException() {
    super("User not found");
  }

}

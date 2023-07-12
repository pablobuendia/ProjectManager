package com.pablobuendia.projectmanager.exception;

public class ProjectAlreadyExistsException extends RuntimeException {

  public ProjectAlreadyExistsException() {
    super("Project already exists");
  }

}

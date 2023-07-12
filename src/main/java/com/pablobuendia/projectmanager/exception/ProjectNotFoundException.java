package com.pablobuendia.projectmanager.exception;

public class ProjectNotFoundException extends ResourceNotFoundException {

  public ProjectNotFoundException() {
    super("Project not found");
  }

}

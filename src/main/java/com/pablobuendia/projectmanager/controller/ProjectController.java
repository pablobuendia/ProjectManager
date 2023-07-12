package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Validated
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping("/projects")
  public List<ProjectDto> getAllProjects(
      @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
      @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
    return projectService.getAllProjects(PageRequest.of(page, size));
  }

  @GetMapping("/projects/search")
  public List<ProjectDto> searchProjects(
      @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
      @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
      @RequestParam(required = false) String name) {
    return projectService.searchProjects(PageRequest.of(page, size), name);
  }

  @PostMapping("/projects")
  public ProjectDto createProject(@RequestBody @Valid final ProjectDto project) {
    return projectService.createProject(project);
  }

  @GetMapping("/projects/{id}")
  public ProjectDto getProjectById(@PathVariable @PositiveOrZero final Long id) {
    return projectService.getProjectById(id);
  }

  @PutMapping("/projects/{id}")
  public ProjectDto updateProject(
      @PathVariable @PositiveOrZero final Long id,
      @RequestBody final ProjectDto project) {
    return projectService.updateProject(id, project);
  }

  @DeleteMapping("/projects/{id}")
  public void deleteProject(@PathVariable @PositiveOrZero final Long id) {
    projectService.deleteProject(id);
  }

  @PostMapping("/projects/{id}/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addUserToProject(
      @PathVariable @PositiveOrZero final Long id,
      @PathVariable @PositiveOrZero final Long userId) {
    projectService.addUserToProject(id, userId);
  }
}

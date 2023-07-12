package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping("/projects")
  public List<ProjectDto> getAllProjects(
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "10") final int size) {
    return projectService.getAllProjects(PageRequest.of(page, size));
  }

  @PostMapping("/projects")
  public ProjectDto createProject(@RequestBody final ProjectDto project) {
    return projectService.createProject(project);
  }

  @GetMapping("/projects/{id}")
  public ProjectDto getProjectById(@PathVariable final Long id) {
    return projectService.getProjectById(id);
  }

  @PutMapping("/projects/{id}")
  public ProjectDto updateProject(@PathVariable final Long id,
      @RequestBody final ProjectDto project) {
    return projectService.updateProject(id, project);
  }

  @DeleteMapping("/projects/{id}")
  public void deleteProject(@PathVariable final Long id) {
    projectService.deleteProject(id);
  }

  @PostMapping("/projects/{id}/users/{userId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void addUserToProject(@PathVariable final Long id, @PathVariable final Long userId) {
    projectService.addUserToProject(id, userId);
  }
}

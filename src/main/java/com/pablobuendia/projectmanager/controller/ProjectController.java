package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.service.ProjectService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProjectController {

  private final ProjectService projectService;

  @GetMapping("/projects")
  public List<ProjectDto> getAllProjects() {
    return projectService.getAllProjects();
  }

  @PostMapping("/projects")
  public ProjectDto createProject(@RequestBody ProjectDto project) {
    return projectService.createProject(project);
  }

  @GetMapping("/projects/{id}")
  public ProjectDto getProjectById(@PathVariable Long id) {
    return projectService.getProjectById(id);
  }

  @PutMapping("/projects/{id}")
  public ProjectDto updateProject(@PathVariable Long id, @RequestBody ProjectDto project) {
    return projectService.updateProject(id, project);
  }

  @DeleteMapping("/projects/{id}")
  public void deleteProject(@PathVariable Long id) {
    projectService.deleteProject(id);
  }
}

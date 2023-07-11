package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.Project;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
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

  private final ProjectRepository projectRepository;

  @GetMapping("/projects")
  public List<Project> getAllProjects() {
    return projectRepository.findAll().stream().map(project -> {
      project.setUsers(null);
      return project;
    }).toList();
  }

  @PostMapping("/projects")
  public Project createProject(@RequestBody Project project) {
    return projectRepository.save(project);
  }

  @GetMapping("/projects/{id}")
  public Project getProjectById(@PathVariable Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
  }

  @PutMapping("/projects/{id}")
  public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
    val projectToUpdate = projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    projectToUpdate.setName(project.getName());
    projectToUpdate.setDescription(project.getDescription());
    return projectRepository.save(projectToUpdate);
  }

  @DeleteMapping("/projects/{id}")
  public void deleteProject(@PathVariable Long id) {
    projectRepository.deleteById(id);
  }
}

package com.pablobuendia.projectmanager.service;


import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.Project;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;

  public List<Project> getAllProjects() {
    return projectRepository.findAll().stream().map(project -> {
      project.setUsers(null);
      return project;
    }).toList();
  }

  public Project createProject(Project project) {
    return projectRepository.save(project);
  }

  public Project getProjectById(Long id) {
    return projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
  }

  public Project updateProject(Long id, Project project) {
    Project projectToUpdate = projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    projectToUpdate.setName(project.getName());
    projectToUpdate.setDescription(project.getDescription());
    return projectRepository.save(projectToUpdate);
  }

  public void deleteProject(Long id) {
    projectRepository.deleteById(id);
  }
}

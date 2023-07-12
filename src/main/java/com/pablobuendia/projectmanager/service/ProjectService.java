package com.pablobuendia.projectmanager.service;


import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.Project;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

  private final ProjectRepository projectRepository;
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;

  public List<ProjectDto> getAllProjects(final Pageable pageable) {
    return projectRepository.findAll(pageable).stream().map(project -> {
      project.setUsers(null);
      return project;
    }).map(project -> modelMapper.map(project, ProjectDto.class)).toList();
  }

  public ProjectDto createProject(final ProjectDto project) {
    return modelMapper
        .map(
            projectRepository.save(modelMapper.map(project, Project.class)),
            ProjectDto.class);
  }

  public ProjectDto getProjectById(final Long id) {
    return modelMapper.map(projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found")), ProjectDto.class);
  }

  public ProjectDto updateProject(final Long id, final ProjectDto project) {
    val projectToUpdate = projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));
    projectToUpdate.setName(project.getName());
    projectToUpdate.setDescription(project.getDescription());
    return modelMapper.map(projectRepository.save(projectToUpdate), ProjectDto.class);
  }

  public void deleteProject(final Long id) {
    projectRepository.deleteById(id);
  }

  public void addUserToProject(final Long id, final Long userId) {
    val project = projectRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Project not found"));

    userRepository
        .findById(userId)
        .ifPresentOrElse(user -> {
          project.getUsers().add(user);
          projectRepository.save(project);
        }, () -> new ResourceNotFoundException("User not found"));
  }
}
package com.pablobuendia.projectmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.model.Project;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

  private static final long PROJECT_ID = 2L;
  private static final String PROJECT_NAME = "Project 1";
  private static final String PROJECT_DESCRIPTION = "Project Description 1";
  private static final long USER_ID = 1L;
  private static final String USER_NAME = "Pablo";
  private static final String USER_EMAIL = "pablo@projectmanager.com";
  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private ProjectService projectService;

  @Test
  void getAllProjects() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Project project = buildProject(Set.of(user));
    ProjectDto projectDto = buildProjectDto(null);

    Page<Project> projectsPage = new PageImpl<>(List.of(project));
    when(projectRepository.findAll(PageRequest.of(0, 10))).thenReturn(projectsPage);
    when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

    List<ProjectDto> projects = projectService.getAllProjects(Pageable.ofSize(10));

    assertEquals(1, projects.size());
    assertEquals(PROJECT_ID, projects.get(0).getId());
    assertEquals(PROJECT_NAME, projects.get(0).getName());
    assertEquals("Project Description 1", projects.get(0).getDescription());

    assertNull(projects.get(0).getUsers());
  }

  @Test
  void createProject() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Project project = buildProject(Set.of(user));
    ProjectDto projectDto = buildProjectDto(Set.of(userDto));

    when(projectRepository.save(project)).thenReturn(project);
    when(modelMapper.map(projectDto, Project.class)).thenReturn(project);
    when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

    ProjectDto response = projectService.createProject(projectDto);

    assertEquals(PROJECT_ID, response.getId());
    assertEquals(PROJECT_NAME, response.getName());
    assertEquals("Project Description 1", response.getDescription());

    assertEquals(1, response.getUsers().size());
    UserDto userFromProject = response.getUsers().iterator().next();
    assertEquals(USER_ID, userFromProject.getId());
    assertEquals(USER_NAME, userFromProject.getName());
    assertEquals(USER_EMAIL, userFromProject.getEmail());
  }

  @Test
  void getProjectById() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Project project = buildProject(Set.of(user));
    ProjectDto projectDto = buildProjectDto(Set.of(userDto));

    when(projectRepository.findById(2L)).thenReturn(Optional.of(project));
    when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

    ProjectDto response = projectService.getProjectById(2L);

    assertEquals(PROJECT_ID, response.getId());
    assertEquals(PROJECT_NAME, response.getName());
    assertEquals(PROJECT_DESCRIPTION, response.getDescription());

    assertEquals(1, response.getUsers().size());
    UserDto userFromProject = response.getUsers().iterator().next();
    assertEquals(USER_ID, userFromProject.getId());
    assertEquals(USER_NAME, userFromProject.getName());
    assertEquals(USER_EMAIL, userFromProject.getEmail());
  }

  @Test
  void updateProject() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Project project = buildProject(Set.of(user));
    ProjectDto projectDto = buildProjectDto(Set.of(userDto));

    when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenReturn(project);
    when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

    ProjectDto response = projectService.updateProject(PROJECT_ID, projectDto);

    assertEquals(PROJECT_ID, response.getId());
    assertEquals(PROJECT_NAME, response.getName());
    assertEquals(PROJECT_DESCRIPTION, response.getDescription());

    assertEquals(1, response.getUsers().size());
    UserDto userFromProject = response.getUsers().iterator().next();
    assertEquals(USER_ID, userFromProject.getId());
    assertEquals(USER_NAME, userFromProject.getName());
    assertEquals(USER_EMAIL, userFromProject.getEmail());
  }

  @Test
  void deleteProject() {
    projectService.deleteProject(PROJECT_ID);

    verify(projectRepository, times(1)).deleteById(PROJECT_ID);
  }

  @Test
  void addUserToProject() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Project project = buildProject(new HashSet<>());
    ProjectDto projectDto = buildProjectDto(new HashSet<>());

    when(projectRepository.findById(PROJECT_ID)).thenReturn(Optional.of(project));
    when(projectRepository.save(project)).thenReturn(project);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));

    projectService.addUserToProject(PROJECT_ID, USER_ID);

    verify(projectRepository, times(1)).save(project);
  }

  @Test
  void searchValidProjects() {
    User user = buildUser();

    Project project = buildProject(Set.of(user));
    ProjectDto projectDto = buildProjectDto(null);

    Page<Project> projectPage = new PageImpl<>(List.of(project));

    when(projectRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(
        projectPage);
    when(modelMapper.map(project, ProjectDto.class)).thenReturn(projectDto);

    List<ProjectDto> response = projectService.searchProjects(Pageable.ofSize(10), PROJECT_NAME);

    assertEquals(1, response.size());
    assertNull(response.get(0).getUsers());
  }

  @Test
  void searchProjectEmpty() {
    User user = buildUser();

    when(projectRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(
        Page.empty());

    List<ProjectDto> response = projectService.searchProjects(Pageable.ofSize(10), "Invalid name");

    assertEquals(0, response.size());
  }

  private static User buildUser() {
    User user = new User();
    user.setId(USER_ID);
    user.setName(USER_NAME);
    user.setEmail(USER_EMAIL);
    return user;
  }

  private static UserDto buildUserDto() {
    UserDto user = new UserDto();
    user.setId(USER_ID);
    user.setName(USER_NAME);
    user.setEmail(USER_EMAIL);
    return user;
  }

  private static Project buildProject(Set<User> users) {
    Project project = new Project();
    project.setId(PROJECT_ID);
    project.setName(PROJECT_NAME);
    project.setDescription(PROJECT_DESCRIPTION);
    project.setUsers(users);
    return project;
  }

  private static ProjectDto buildProjectDto(Set<UserDto> users) {
    ProjectDto project = new ProjectDto();
    project.setId(PROJECT_ID);
    project.setName(PROJECT_NAME);
    project.setDescription(PROJECT_DESCRIPTION);
    project.setUsers(users);
    return project;
  }
}
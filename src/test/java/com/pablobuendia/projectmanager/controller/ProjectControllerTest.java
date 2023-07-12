package com.pablobuendia.projectmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablobuendia.projectmanager.exception.ProjectAlreadyExistsException;
import com.pablobuendia.projectmanager.exception.ProjectNotFoundException;
import com.pablobuendia.projectmanager.exception.UserNotFoundException;
import com.pablobuendia.projectmanager.model.Project;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.service.ProjectService;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProjectController.class)
class ProjectControllerTest {

  private static final long PROJECT_ID = 2L;
  private static final String PROJECT_NAME = "Project 1";
  private static final String PROJECT_DESCRIPTION = "Project Description 1";
  private static final long USER_ID = 1L;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private ProjectService projectService;

  @Test
  void getAllProjects() throws Exception {
    mockMvc.perform(get("/api/v1/projects")
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void getAllProjectsWithPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/projects")
            .contentType("application/json")
            .queryParam("page", "0")
            .queryParam("size", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void getAllProjectsWithInvalidPage() throws Exception {
    mockMvc.perform(get("/api/v1/projects")
            .contentType("application/json")
            .queryParam("page", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllProjectsWithInvalidSize() throws Exception {
    mockMvc.perform(get("/api/v1/projects")
            .contentType("application/json")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchProjects() throws Exception {
    mockMvc.perform(get("/api/v1/projects/search")
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void searchProjectsWithPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/projects/search")
            .contentType("application/json")
            .queryParam("page", "0")
            .queryParam("size", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void searchProjectsWithInvalidPage() throws Exception {
    mockMvc.perform(get("/api/v1/projects/search")
            .contentType("application/json")
            .queryParam("page", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchProjectsWithInvalidSize() throws Exception {
    mockMvc.perform(get("/api/v1/projects/search")
            .contentType("application/json")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }


  @Test
  void createProject() throws Exception {
    mockMvc.perform(post("/api/v1/projects")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(buildProject(null))
        )).andExpect(status().isOk());
  }

  @Test
  void createProjectBadRequest() throws Exception {
    Project project = buildProject(null);
    project.setName(null);

    mockMvc.perform(post("/api/v1/projects")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(project)
        )).andExpect(status().isBadRequest());
  }

  @Test
  void createProjectAlreadyCreated() throws Exception {
    when(projectService.createProject(any())).thenThrow(
        new ProjectAlreadyExistsException());

    mockMvc.perform(post("/api/v1/projects")
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(buildProject(null))
        )).andExpect(status().isConflict());
  }

  @Test
  void getProjectById() throws Exception {
    mockMvc.perform(get("/api/v1/projects/{id}", PROJECT_ID)
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void getProjectByIdBadRequest() throws Exception {
    mockMvc.perform(get("/api/v1/projects/{id}", -1)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getProjectByIdNotFound() throws Exception {
    when(projectService.getProjectById(any())).thenThrow(
        new ProjectNotFoundException());

    mockMvc.perform(get("/api/v1/projects/{id}", PROJECT_ID)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateProject() throws Exception {
    mockMvc.perform(put("/api/v1/projects/{id}", PROJECT_ID)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(buildProject(null))
        )).andExpect(status().isOk());
  }

  @Test
  void updateProjectBadRequest() throws Exception {
    Project project = buildProject(null);
    project.setName(null);

    when(projectService.updateProject(eq(PROJECT_ID), any())).thenThrow(
        new ProjectNotFoundException());

    mockMvc.perform(put("/api/v1/projects/{id}", PROJECT_ID)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(project)
        )).andExpect(status().isNotFound());
  }

  @Test
  void updateProjectBadId() throws Exception {
    Project project = buildProject(null);

    mockMvc.perform(put("/api/v1/projects/{id}", -1)
        .contentType("application/json")
        .content(objectMapper.writeValueAsString(project)
        )).andExpect(status().isBadRequest());
  }

  @Test
  void deleteProject() throws Exception {
    mockMvc.perform(delete("/api/v1/projects/{id}", PROJECT_ID)
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteProjectBadRequest() throws Exception {
    mockMvc.perform(delete("/api/v1/projects/{id}", -1)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteProjectNotFound() throws Exception {
    doThrow(new ProjectNotFoundException()).when(projectService).deleteProject(any());

    mockMvc.perform(delete("/api/v1/projects/{id}", PROJECT_ID)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void addUserToProject() throws Exception {
    mockMvc.perform(post("/api/v1/projects/{id}/users/{userId}", PROJECT_ID, USER_ID)
            .contentType("application/json"))
        .andExpect(status().isNoContent());
  }

  @Test
  void addUserToProjectBadRequestProjectId() throws Exception {
    mockMvc.perform(post("/api/v1/projects/{id}/users/{userId}", -1, USER_ID)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void addUserToProjectBadRequestUserId() throws Exception {
    mockMvc.perform(post("/api/v1/projects/{id}/users/{userId}", PROJECT_ID, -1)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void addUserToProjectNotFoundProject() throws Exception {
    doThrow(new ProjectNotFoundException()).when(projectService).addUserToProject(any(), any());

    mockMvc.perform(post("/api/v1/projects/{id}/users/{userId}", PROJECT_ID, USER_ID)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void addUserToProjectNotFoundUser() throws Exception {
    doThrow(new UserNotFoundException()).when(projectService).addUserToProject(any(), any());

    mockMvc.perform(post("/api/v1/projects/{id}/users/{userId}", PROJECT_ID, USER_ID)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }


  private static Project buildProject(Set<User> users) {
    Project project = new Project();
    project.setId(PROJECT_ID);
    project.setName(PROJECT_NAME);
    project.setDescription(PROJECT_DESCRIPTION);
    project.setUsers(users);
    return project;
  }
}
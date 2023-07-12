package com.pablobuendia.projectmanager.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.exception.UserAlreadyExistsException;
import com.pablobuendia.projectmanager.exception.UserNotFoundException;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

  private static final long PROJECT_ID = 2L;
  private static final String PROJECT_NAME = "Project 1";
  private static final String PROJECT_DESCRIPTION = "Project Description 1";
  private static final long USER_ID = 1L;
  private static final String USER_NAME = "Pablo";
  private static final String USER_EMAIL = "pablo@projectmanager.com";

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;

  @Test
  void getAllUsers() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void getAllUsersWithPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .contentType("application/json")
            .queryParam("page", "0")
            .queryParam("size", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void getAllUsersWithInvalidPage() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .contentType("application/json")
            .queryParam("page", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllUsersWithInvalidSize() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .contentType("application/json")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getAllUsersWithInvalidPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/users")
            .contentType("application/json")
            .queryParam("page", "-1")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchUsers() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void searchUsersWithPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("page", "0")
            .queryParam("size", "1"))
        .andExpect(status().isOk());
  }

  @Test
  void searchUsersWithInvalidPage() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("page", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchUsersWithInvalidSize() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchUsersWithInvalidPageAndSize() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("page", "-1")
            .queryParam("size", "-1"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void searchUsersWithEmail() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("email", USER_EMAIL))
        .andExpect(status().isOk());
  }

  @Test
  void searchUsersWithName() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("name", USER_NAME))
        .andExpect(status().isOk());
  }

  @Test
  void searchUsersWithInvalidEmail() throws Exception {
    mockMvc.perform(get("/api/v1/users/search")
            .contentType("application/json")
            .queryParam("email", "text"))
        .andExpect(status().isOk());
  }

  @Test
  void createUser() throws Exception {
    User user = buildUser();
    mockMvc.perform(post("/api/v1/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk());
  }

  @Test
  void createUserWithInvalidEmail() throws Exception {
    User user = buildUser();
    user.setEmail("invalid");
    mockMvc.perform(post("/api/v1/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void createUserAlreadyExists() throws Exception {
    User user = buildUser();
    when(userService.createUser(any(UserDto.class))).thenThrow(new UserAlreadyExistsException());

    mockMvc.perform(post("/api/v1/users")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isConflict());
  }

  @Test
  void getUserById() throws Exception {
    mockMvc.perform(get("/api/v1/users/{id}", USER_ID)
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void getUserByIdWithInvalidId() throws Exception {
    mockMvc.perform(get("/api/v1/users/{id}", -1)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void getUserByIdWithNonExistingId() throws Exception {
    when(userService.getUserById(anyLong())).thenThrow(new UserNotFoundException());

    mockMvc.perform(get("/api/v1/users/{id}", 0)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  @Test
  void updateUser() throws Exception {
    User user = buildUser();
    mockMvc.perform(put("/api/v1/users/{id}", USER_ID)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk());
  }

  @Test
  void updateUserWithInvalidId() throws Exception {
    User user = buildUser();
    mockMvc.perform(put("/api/v1/users/{id}", -1)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateUserWithNonExistingId() throws Exception {
    User user = buildUser();
    when(userService.updateUser(anyLong(), any(UserDto.class))).thenReturn(new UserDto());

    mockMvc.perform(put("/api/v1/users/{id}", 0)
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(status().isOk());
  }

  @Test
  void deleteUser() throws Exception {
    mockMvc.perform(delete("/api/v1/users/{id}", USER_ID)
            .contentType("application/json"))
        .andExpect(status().isOk());
  }

  @Test
  void deleteUserWithInvalidId() throws Exception {
    mockMvc.perform(delete("/api/v1/users/{id}", -1)
            .contentType("application/json"))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteUserWithNonExistingId() throws Exception {
    doThrow(new UserNotFoundException()).when(userService).deleteUser(anyLong());

    mockMvc.perform(delete("/api/v1/users/{id}", 0)
            .contentType("application/json"))
        .andExpect(status().isNotFound());
  }

  private static User buildUser() {
    User user = new User();
    user.setId(USER_ID);
    user.setName(USER_NAME);
    user.setEmail(USER_EMAIL);
    return user;
  }
}
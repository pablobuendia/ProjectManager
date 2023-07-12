package com.pablobuendia.projectmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pablobuendia.projectmanager.dto.ProjectDto;
import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.exception.UserAlreadyExistsException;
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
class UserServiceTest {


  private static final long PROJECT_ID = 2L;
  private static final String PROJECT_NAME = "Project 1";
  private static final long USER_ID = 1L;
  private static final String USER_NAME = "Pablo";
  private static final String USER_EMAIL = "pablo@projectmanager.com";

  @Mock
  private UserRepository userRepository;
  @Mock
  private ProjectRepository projectRepository;
  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private UserService userService;

  @Test
  void getAllUsers() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Page<User> usersPage = new PageImpl<>(List.of(user));
    when(userRepository.findAll(PageRequest.of(0, 10))).thenReturn(usersPage);
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    List<UserDto> users = userService.getAllUsers(Pageable.ofSize(10));

    assertEquals(1, users.size());
    assertEquals(USER_ID, users.get(0).getId());
    assertEquals(USER_NAME, users.get(0).getName());
    assertEquals(USER_EMAIL, users.get(0).getEmail());
  }

  @Test
  void createUser() {
    UserDto userDto = buildUserDto();
    User user = buildUser();

    when(modelMapper.map(userDto, User.class)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    UserDto response = userService.createUser(userDto);

    assertEquals(USER_ID, response.getId());
    assertEquals(USER_NAME, response.getName());
    assertEquals(USER_EMAIL, response.getEmail());
  }

  @Test
  void createUserAlreadyExists() {
    UserDto userDto = buildUserDto();
    User user = buildUser();

    when(userRepository.existsByEmail(USER_EMAIL)).thenReturn(true);

    assertThrows(UserAlreadyExistsException.class, () -> userService.createUser(userDto));
  }

  @Test
  void getUserById() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(user));
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    UserDto response = userService.getUserById(USER_ID);

    assertEquals(USER_ID, response.getId());
    assertEquals(USER_NAME, response.getName());
    assertEquals(USER_EMAIL, response.getEmail());
  }

  @Test
  void updateUser() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    when(userRepository.existsById(USER_ID)).thenReturn(true);
    when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
    when(userRepository.save(user)).thenReturn(user);
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    UserDto response = userService.updateUser(USER_ID, userDto);

    assertEquals(USER_ID, response.getId());
    assertEquals(USER_NAME, response.getName());
    assertEquals(USER_EMAIL, response.getEmail());
  }

  @Test
  void updateUserNotExistingId() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    when(userRepository.existsById(USER_ID)).thenReturn(false);
    when(userRepository.save(user)).thenReturn(user);
    when(modelMapper.map(userDto, User.class)).thenReturn(user);
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    UserDto response = userService.updateUser(USER_ID, userDto);

    assertEquals(USER_ID, response.getId());
    assertEquals(USER_NAME, response.getName());
    assertEquals(USER_EMAIL, response.getEmail());
  }

  @Test
  void deleteUser() {
    User user = buildUser();

    Set<User> users = new HashSet<>();
    users.add(user);
    Project project = buildProject(users);

    when(userRepository.existsById(USER_ID)).thenReturn(true);
    when(projectRepository.findAll()).thenReturn(List.of(project));

    userService.deleteUser(USER_ID);

    verify(projectRepository).save(argThat(p -> p.getUsers().isEmpty()));
    verify(userRepository).deleteById(USER_ID);
  }

  @Test
  void searchValidUsers() {
    User user = buildUser();
    UserDto userDto = buildUserDto();

    Page<User> usersPage = new PageImpl<>(List.of(user));

    when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(
        usersPage);
    when(modelMapper.map(user, UserDto.class)).thenReturn(userDto);

    List<UserDto> response = userService.searchUsers(Pageable.ofSize(10), USER_NAME, USER_EMAIL);

    assertEquals(1, response.size());
    assertEquals(USER_ID, response.get(0).getId());
    assertEquals(USER_NAME, response.get(0).getName());
    assertEquals(USER_EMAIL, response.get(0).getEmail());
  }

  @Test
  void searchUsersEmpty() {
    User user = buildUser();

    when(userRepository.findAll(any(Specification.class), eq(PageRequest.of(0, 10)))).thenReturn(
        Page.empty());

    List<UserDto> response = userService.searchUsers(Pageable.ofSize(10), "Invalid name",
        "Invalid email");

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
    project.setDescription("Project Description 1");
    project.setUsers(users);
    return project;
  }

  private static ProjectDto buildProjectDto(Set<UserDto> users) {
    ProjectDto project = new ProjectDto();
    project.setId(PROJECT_ID);
    project.setName(PROJECT_NAME);
    project.setDescription("Project Description 1");
    project.setUsers(users);
    return project;
  }

}
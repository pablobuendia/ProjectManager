package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  public List<UserDto> getAllUsers(
      @RequestParam(defaultValue = "0") final int page,
      @RequestParam(defaultValue = "10") final int size) {
    return userService.getAllUsers(PageRequest.of(page, size));
  }

  @PostMapping("/users")
  public UserDto createUser(@RequestBody final UserDto user) {
    return userService.createUser(user);
  }

  @GetMapping("/users/{id}")
  public UserDto getUserById(@PathVariable final Long id) {
    return userService.getUserById(id);
  }

  @PutMapping("/users/{id}")
  public UserDto updateUser(@PathVariable final Long id, @RequestBody final UserDto user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable final Long id) {
    userService.deleteUser(id);
  }
}

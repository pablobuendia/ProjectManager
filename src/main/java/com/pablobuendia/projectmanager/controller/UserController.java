package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.service.UserService;
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
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  public List<UserDto> getAllUsers() {
    return userService.getAllUsers();
  }

  @PostMapping("/users")
  public UserDto createUser(@RequestBody UserDto user) {
    return userService.createUser(user);
  }

  @GetMapping("/users/{id}")
  public UserDto getUserById(@PathVariable Long id) {
    return userService.getUserById(id);
  }

  @PutMapping("/users/{id}")
  public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }
}

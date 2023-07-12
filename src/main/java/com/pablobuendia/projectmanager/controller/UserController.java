package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class UserController {

  private final UserService userService;

  @GetMapping("/users")
  public List<UserDto> getAllUsers(
      @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
      @RequestParam(defaultValue = "10") @PositiveOrZero final int size) {
    return userService.getAllUsers(PageRequest.of(page, size));
  }

  @GetMapping("/users/search")
  public List<UserDto> searchUsers(
      @RequestParam(defaultValue = "0") @PositiveOrZero final int page,
      @RequestParam(defaultValue = "10") @PositiveOrZero final int size,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) @Email String email) {
    return userService.searchUsers(PageRequest.of(page, size), name, email);
  }

  @PostMapping("/users")
  public UserDto createUser(@RequestBody @Valid final UserDto user) {
    return userService.createUser(user);
  }

  @GetMapping("/users/{id}")
  public UserDto getUserById(@PathVariable @PositiveOrZero final Long id) {
    return userService.getUserById(id);
  }

  @PutMapping("/users/{id}")
  public UserDto updateUser(
      @PathVariable @PositiveOrZero final Long id,
      @RequestBody @Valid final UserDto user) {
    return userService.updateUser(id, user);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable @PositiveOrZero final Long id) {
    userService.deleteUser(id);
  }
}

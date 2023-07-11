package com.pablobuendia.projectmanager.controller;

import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
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
public class UserController {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  @GetMapping("/users")
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @PostMapping("/users")
  public User createUser(@RequestBody User user) {
    return userRepository.save(user);
  }

  @GetMapping("/users/{id}")
  public User getUserById(@PathVariable Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  @PutMapping("/users/{id}")
  public User updateUser(@PathVariable Long id, @RequestBody User user) {
    val userToUpdate = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userToUpdate.setName(user.getName());
    userToUpdate.setEmail(user.getEmail());
    return userRepository.save(userToUpdate);
  }

  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable Long id) {
    projectRepository.findAll().forEach(project -> {
      project.getUsers().removeIf(user -> user.getId().equals(id));
      projectRepository.save(project);
    });
    userRepository.deleteById(id);
  }
}

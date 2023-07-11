package com.pablobuendia.projectmanager.service;


import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User createUser(User user) {
    return userRepository.save(user);
  }

  public User getUserById(Long id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  public User updateUser(Long id, User user) {
    User userToUpdate = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    userToUpdate.setName(user.getName());
    userToUpdate.setEmail(user.getEmail());
    return userRepository.save(userToUpdate);
  }

  public void deleteUser(Long id) {
    projectRepository.findAll().forEach(project -> {
      project.getUsers().removeIf(user -> user.getId().equals(id));
      projectRepository.save(project);
    });
    userRepository.deleteById(id);
  }
}

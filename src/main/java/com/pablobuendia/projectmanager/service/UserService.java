package com.pablobuendia.projectmanager.service;


import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.exception.ResourceNotFoundException;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final ModelMapper modelMapper;

  public List<UserDto> getAllUsers() {
    return userRepository.findAll().stream().map(user -> modelMapper.map(user, UserDto.class))
        .toList();
  }

  public UserDto createUser(final UserDto userDto) {
    return modelMapper.map(userRepository.save(modelMapper.map(userDto, User.class)),
        UserDto.class);
  }

  public UserDto getUserById(final Long id) {
    return modelMapper.map(userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found")), UserDto.class);
  }

  public UserDto updateUser(final Long id, final UserDto userDto) {
    val userToUpdate = userRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    userToUpdate.setName(userDto.getName());
    userToUpdate.setEmail(userDto.getEmail());
    return modelMapper.map(userRepository.save(userToUpdate), UserDto.class);
  }

  public void deleteUser(final Long id) {
    projectRepository.findAll().forEach(project -> {
      project.getUsers().removeIf(user -> user.getId().equals(id));
      projectRepository.save(project);
    });
    userRepository.deleteById(id);
  }
}

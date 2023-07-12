package com.pablobuendia.projectmanager.service;


import com.pablobuendia.projectmanager.dto.UserDto;
import com.pablobuendia.projectmanager.exception.UserNotFoundException;
import com.pablobuendia.projectmanager.model.User;
import com.pablobuendia.projectmanager.repository.ProjectRepository;
import com.pablobuendia.projectmanager.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final ProjectRepository projectRepository;
  private final ModelMapper modelMapper;

  public List<UserDto> getAllUsers(final Pageable pageable) {
    return userRepository.findAll(pageable).stream()
        .map(user -> modelMapper.map(user, UserDto.class))
        .toList();
  }

  public UserDto createUser(final UserDto userDto) {
    return modelMapper.map(userRepository.save(modelMapper.map(userDto, User.class)),
        UserDto.class);
  }

  public UserDto getUserById(final Long id) {
    return modelMapper.map(userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new), UserDto.class);
  }

  public UserDto updateUser(final Long id, final UserDto userDto) {
    val userToUpdate = userRepository.findById(id)
        .orElseThrow(UserNotFoundException::new);

    userToUpdate.setName(userDto.getName());
    userToUpdate.setEmail(userDto.getEmail());
    return modelMapper.map(userRepository.save(userToUpdate), UserDto.class);
  }

  public void deleteUser(final Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException();
    }
    projectRepository.findAll().forEach(project -> {
      project.getUsers().removeIf(user -> user.getId().equals(id));
      projectRepository.save(project);
    });
    userRepository.deleteById(id);
  }

  public List<UserDto> searchUsers(final Pageable pageable, final String name, final String email) {
    Specification<User> spec = Specification.where(null);

    if (StringUtils.hasText(name)) {
      spec = spec.and((root, query, criteriaBuilder) ->
          criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
              "%" + name.toLowerCase() + "%"));
    }

    if (StringUtils.hasText(email)) {
      spec = spec.and((root, query, criteriaBuilder) ->
          criteriaBuilder.like(criteriaBuilder.lower(root.get("email")),
              "%" + email.toLowerCase() + "%"));
    }

    return userRepository.findAll(spec, pageable).stream()
        .map(user -> modelMapper.map(user, UserDto.class))
        .toList();
  }
}

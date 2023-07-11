package com.pablobuendia.projectmanager.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Generated
public class ProjectDto {

  private Long id;

  private String name;

  private String description;

  private Set<UserDto> users;

}

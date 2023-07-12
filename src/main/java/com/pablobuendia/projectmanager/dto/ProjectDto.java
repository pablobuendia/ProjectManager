package com.pablobuendia.projectmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Generated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {

  private Long id;

  @NotBlank(message = "Project name is mandatory")
  private String name;

  private String description;

  private Set<UserDto> users;

}

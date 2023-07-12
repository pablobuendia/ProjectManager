package com.pablobuendia.projectmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private Long id;

  private String name;

  private String description;

  private Set<UserDto> users;

}

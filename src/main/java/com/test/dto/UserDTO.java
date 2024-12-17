package com.test.dto;

import java.time.LocalDate;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.models.constants.Gender;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String username;

  private String password;

  private String firstName;

  private String lastName;

  private String email;

  private String mobile;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  private Gender gender;

  private Set<String> roles;

  private Set<String> groups;

}

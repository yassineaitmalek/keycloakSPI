package com.test.models;

import java.time.LocalDate;
import java.util.Set;

import org.keycloak.models.UserModel;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

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
public class UserDetails {

  private UserRepresentation userRepresentation;

  private Set<RoleRepresentation> roleRepresentations;

  private Set<GroupRepresentation> groupRepresentations;

  private String mobile;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  private Gender gender;

}

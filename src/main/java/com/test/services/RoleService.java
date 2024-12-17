package com.test.services;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import com.test.dto.RoleDTO;

public class RoleService {

  private final KeycloakSession session;

  private final RealmModel realmModel;

  private final UserProvider userProvider;

  private final String realmName;

  public RoleService(KeycloakSession session) {
    this.session = session;
    this.userProvider = session.users();
    this.realmName = session.getContext().getRealm().getName();
    this.realmModel = session.realms().getRealmByName(this.realmName);

  }

  public Set<RoleRepresentation> getRoles() {

    return realmModel.getRoles()
        .stream()
        .map(ModelToRepresentation::toRepresentation)
        .collect(Collectors.toSet());

  }

  public Set<RoleRepresentation> getRoleByUserId(String id) {
    return Optional.ofNullable(id)
        .map(e -> userProvider.getUserById(e, realmModel))
        .map(this::getRoleByUserModel)
        .orElseGet(Collections::emptySet);
  }

  public Set<RoleRepresentation> getRoleByUserModel(UserModel userModel) {
    return Optional.ofNullable(userModel)
        .map(UserModel::getRealmRoleMappings)
        .orElseGet(Collections::emptySet)
        .stream()
        .map(ModelToRepresentation::toRepresentation)
        .collect(Collectors.toSet());

  }

  public Set<RoleModel> getRoles(Set<String> roles) {
    return Optional.ofNullable(roles)
        .orElseGet(Collections::emptySet)
        .stream()
        .map(realmModel::getRole)
        .collect(Collectors.toSet());
  }

  public UserModel grantRoles(UserModel userModel, Set<String> roles) {
    getRoles(roles).stream().forEach(userModel::grantRole);
    return userModel;
  }

  public RoleRepresentation createRole(RoleDTO roleDTO) {

    if (Objects.nonNull(realmModel.getRole(roleDTO.getName()))) {
      throw new IllegalStateException("Role already exists: " + roleDTO.getName());
    }
    RoleModel role = realmModel.addRole(roleDTO.getName());
    role.setDescription(roleDTO.getName());

    return ModelToRepresentation.toRepresentation(role);
  }

  public void deleteRoleById(String roleId) {

    RoleModel role = realmModel.getRoleById(roleId);
    if (Objects.isNull(role)) {
      throw new IllegalStateException("Role not found for ID: " + roleId);
    }
    realmModel.removeRole(role);

  }

  public void removeRoleFromUser(String userId, String roleId) {

    UserModel user = userProvider.getUserById(userId, realmModel);

    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("User not found with ID: " + userId);
    }

    RoleModel role = realmModel.getRoleById(roleId);
    if (Objects.isNull(role)) {
      throw new IllegalArgumentException("Role not found: " + roleId);
    }

    if (user.hasRole(role)) {
      user.deleteRoleMapping(role);

    }
  }

  public void addRoleFromUser(String userId, String roleId) {

    UserModel user = userProvider.getUserById(userId, realmModel);

    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("User not found with ID: " + userId);
    }

    RoleModel role = realmModel.getRoleById(roleId);
    if (Objects.isNull(role)) {
      throw new IllegalArgumentException("Role not found: " + roleId);
    }

    if (!user.hasRole(role)) {
      user.grantRole(role);

    }
  }

}

package com.test.services;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

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

  public Set<RoleRepresentation> getRoleByUserName(String username) {

    return Optional.ofNullable(username)
        .map(e -> userProvider.getUserByUsername(e, realmModel))
        .map(UserModel::getRealmRoleMappings)
        .map(this::convert)
        .orElseThrow(() -> new RuntimeException("roles not found"));

  }

  public Set<RoleRepresentation> getRoleByUserId(String id) {
    return Optional.ofNullable(id)
        .map(e -> userProvider.getUserById(e, realmModel))
        .map(UserModel::getRealmRoleMappings)
        .map(this::convert)
        .orElseThrow(() -> new RuntimeException("roles not found"));

  }

  public Set<RoleRepresentation> convert(Set<RoleModel> roles) {
    return roles.stream()
        .map(ModelToRepresentation::toRepresentation)
        .collect(Collectors.toSet());

  }

}

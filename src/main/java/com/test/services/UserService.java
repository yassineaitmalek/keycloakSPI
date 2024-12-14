package com.test.services;

import java.util.Optional;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.representations.idm.UserRepresentation;

public class UserService {

  private final KeycloakSession session;

  private final RealmModel realmModel;

  private final UserProvider userProvider;

  private final String realmName;

  public UserService(KeycloakSession session) {
    this.session = session;
    this.userProvider = session.users();
    this.realmName = session.getContext().getRealm().getName();
    this.realmModel = session.realms().getRealmByName(this.realmName);

  }

  public UserRepresentation getUserByUserName(String username) {

    return Optional.ofNullable(username)
        .map(e -> userProvider.getUserByUsername(e, realmModel))
        .map(this::convert)
        .orElseThrow(() -> new RuntimeException("user not found"));

  }

  public UserRepresentation getUserById(String id) {
    return Optional.ofNullable(id)
        .map(e -> userProvider.getUserById(e, realmModel))
        .map(this::convert)
        .orElseThrow(() -> new RuntimeException("user not found"));

  }

  public UserRepresentation convert(UserModel user) {
    return ModelToRepresentation.toRepresentation(session, realmModel, user);
  }

}

package com.test.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;

import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityCheck {

  private final KeycloakSession session;

  private final AuthResult authResult;

  private final String realmName;

  private final RealmModel realmModel;

  private final boolean isAuth;

  private final ClientModel clientModel;

  private final String clientId;

  public SecurityCheck(KeycloakSession session, boolean isAuth) {
    this.session = session;
    this.isAuth = isAuth;
    this.realmName = session.getContext().getRealm().getName();
    log.info("security realmName : {}", realmName);
    this.authResult = new AppAuthManager().authenticateBearerToken(session);
    this.realmModel = session.realms().getRealmByName(this.realmName);
    this.clientModel = getClientModel();
    this.clientId = getClientId();
    logUser();
    this.isAuthenticated();

  }

  public void logUser() {

    if (Objects.isNull(authResult)) {
      log.info("Anonymous user entering realm {}", realmName);
      return;
    }
    log.info("{}, realm: {}, client: {}", authResult.getUser().getUsername(), realmName, clientId);
    log.info("Realm roles: {}", getCurrentUserRolesString());

  }

  public UserModel getCurrentUser() {
    return Optional.ofNullable(authResult)
        .map(AuthResult::getUser)
        .orElseThrow(() -> new NotAuthorizedException("there is no current user"));
  }

  public UserRepresentation getCurrentUserRepresentation() {
    return Optional.ofNullable(authResult)
        .map(AuthResult::getUser)
        .map(e -> ModelToRepresentation.toRepresentation(session, realmModel, e))
        .orElseThrow(() -> new NotAuthorizedException("there is no current user"));
  }

  public Set<RoleRepresentation> getCurrentUserRoles() {
    return Optional.ofNullable(getCurrentUser())
        .map(UserModel::getRealmRoleMappings)
        .orElseGet(Collections::emptySet)
        .stream()
        .map(ModelToRepresentation::toRepresentation)
        .collect(Collectors.toSet());
  }

  public Set<String> getCurrentUserRolesString() {
    return getCurrentUserRoles()
        .stream()
        .map(RoleRepresentation::getName)
        .collect(Collectors.toSet());
  }

  public void hasRoles(Set<String> roles) {

    Set<String> userRoles = getCurrentUserRolesString();
    boolean anyMatch = roles.stream().anyMatch(userRoles::contains);
    if (!anyMatch) {
      throw new ForbiddenException("You do not have the required credentials for this action");
    }

  }

  public void hasRoles(String... roles) {

    hasRoles(new HashSet<>(Arrays.asList(roles)));
  }

  public void isClient(String clientName) {

    if (!clientId.equals(clientName)) {
      throw new ForbiddenException("You do not have the required credentials for this action");
    }
  }

  public ClientModel getClientModel() {
    return Optional.ofNullable(authResult)
        .map(e -> realmModel.getClientByClientId(authResult.getToken().getIssuedFor()))
        .orElse(null);

  }

  public String getClientId() {
    return Optional.ofNullable(clientModel)
        .map(ClientModel::getClientId)
        .orElseGet(String::new);
  }

  public void isAuthenticated() {
    if (isAuth && Objects.isNull(authResult)) {

      throw new NotAuthorizedException("token required");

    }

  }
}

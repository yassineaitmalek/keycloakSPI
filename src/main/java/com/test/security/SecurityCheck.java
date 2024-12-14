package com.test.security;

import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;

import org.keycloak.models.ClientModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SecurityCheck {

  private final KeycloakSession session;

  private final AuthResult authResult;

  private final String realmName;

  private final RealmModel realmModel;

  public SecurityCheck(KeycloakSession session) {
    this.session = session;
    this.realmName = session.getContext().getRealm().getName();
    log.info("security realmName : {}", realmName);
    this.authResult = new AppAuthManager().authenticateBearerToken(session);
    this.realmModel = session.realms().getRealmByName(this.realmName);
    logUser();
    this.isAuthenticated();

  }

  public void logUser() {

    if (Objects.isNull(authResult)) {
      log.info("Anonymous user entering realm {}", realmName);
      return;
    }
    ClientModel client = realmModel.getClientByClientId(authResult.getToken().getIssuedFor());
    log.info("{}, realm: {}, client: {}", authResult.getUser().getUsername(), realmName,
        client.getClientId());
    log.info("Realm roles: {}",
        authResult.getUser().getRealmRoleMappings().stream().map(RoleModel::getName).collect(Collectors.toSet()));

  }

  public void hasRole(String role) {

    if (Objects.isNull(authResult.getToken().getRealmAccess())
        || !authResult.getToken().getRealmAccess().isUserInRole(role)) {
      throw new ForbiddenException("You do not have the required credentials for this action");
    }
  }

  public void isClient(KeycloakSession session, String clientName) {

    ClientModel client = realmModel.getClientByClientId(authResult.getToken().getIssuedFor());
    if (!client.getClientId().equals(clientName)) {
      throw new ForbiddenException("You do not have the required credentials for this action");
    }
  }

  public void isAuthenticated() {
    if (Objects.isNull(authResult)) {
      throw new NotAuthorizedException("token required");
    }
  }
}

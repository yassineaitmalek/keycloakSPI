package com.test.keyclock.spi.security;

import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.ClientModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;

@Slf4j
@RequiredArgsConstructor
public class SecurityCheck {

    private final KeycloakSessionWrapper sessionWrapper;

    public void logUser() {
        AuthResult authResult = sessionWrapper.getAuthResult();
        String realmName = sessionWrapper.getRealmName();
        String clientId = getClientId();
        if (Objects.isNull(authResult)) {
            log.info("Anonymous user entering realm {}", realmName);
            return;
        }
        log.info("{}, realm: {}, client: {}", authResult.getUser().getUsername(), realmName, clientId);
        log.info("Realm roles: {}", getCurrentUserRolesString());
    }

    public UserRepresentation getCurrentUser() {
        AuthResult authResult = sessionWrapper.getAuthResult();
        if (Objects.isNull(authResult)) {
            new NotAuthorizedException("there is no current user");
        }
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserModel userModel = authResult.getUser();
        return ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, userModel);
    }

    public Set<RoleRepresentation> getCurrentUserRoles() {
        AuthResult authResult = sessionWrapper.getAuthResult();
        if (Objects.isNull(authResult)) {
            new NotAuthorizedException("there is no current user");
        }
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserModel userModel = authResult.getUser();
        return Optional
            .ofNullable(userModel)
            .map(UserModel::getRealmRoleMappings)
            .orElseGet(Collections::emptySet)
            .stream()
            .map(ModelToRepresentation::toRepresentation)
            .collect(Collectors.toSet());
    }

    private Set<String> getCurrentUserRolesString() {
        return getCurrentUserRoles().stream().map(RoleRepresentation::getName).collect(Collectors.toSet());
    }

    public boolean hasAllRoles(Set<String> roles) {
        Objects.requireNonNull(roles);
        Set<String> userRoles = getCurrentUserRolesString();
        boolean hasRoles = userRoles.containsAll(roles);
        if (!hasRoles) {
            throw new ForbiddenException("You do not have the required credentials for this action");
        }
        return hasRoles;
    }

    public void isClient(String clientName) {
        Objects.requireNonNull(clientName);
        String clientId = getClientId();
        Objects.requireNonNull(clientId);
        if (!clientId.equals(clientName)) {
            throw new ForbiddenException("You do not have the required credentials for this action");
        }
    }

    public String getClientId() {
        ClientModel clientModel = sessionWrapper.getClientModel();
        Objects.requireNonNull(clientModel);
        return clientModel.getClientId();
    }

    public void shouldAuthenticate() {
        if (Objects.isNull(sessionWrapper.getAuthResult())) {
            throw new NotAuthorizedException("token required");
        }
    }
}

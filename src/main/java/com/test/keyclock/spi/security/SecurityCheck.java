package com.test.keyclock.spi.security;


import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotAuthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.models.ClientModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.services.managers.AuthenticationManager.AuthResult;


@Slf4j
@RequiredArgsConstructor
public class SecurityCheck {

	private final KeycloakSessionWrapper sessionWrapper;

	private UserModel userModel;

	private Set<String> userRoles;

	public void logUser() {
		AuthResult authResult = sessionWrapper.getAuthResult();
		String realmName = sessionWrapper.getRealmName();
		String clientId = getClientId();
		if ( Objects.isNull(authResult) ) {
			log.info("Anonymous user entering realm {}", realmName);
			return;
		}
		log.info("{}, realm: {}, client: {}", authResult.getUser().getUsername(), realmName, clientId);
		log.info("Realm roles: {}", userRoles);
	}

	@PostConstruct
	public void initCurrentUserModel() {
		AuthResult authResult = sessionWrapper.getAuthResult();
		if ( Objects.isNull(authResult) ) {
			throw new NotAuthorizedException("there is no current user");
		}

		this.userModel = authResult.getUser();
		this.userRoles = Optional.ofNullable(this.userModel).map(e -> Stream.concat(e.getRealmRoleMappings().stream(), e.getGroups().stream().map(GroupModel::getRoleMappings).flatMap(Set::stream)).distinct().map(RoleModel::getComposites).flatMap(Set::stream).collect(Collectors.toSet())

		).orElseGet(Collections::emptySet).stream().map(ModelToRepresentation::toRepresentation).map(RoleRepresentation::getName).collect(Collectors.toSet());

	}

	public UserRepresentation getCurrentUserRepresentation() {
		RealmModel realmModel = sessionWrapper.getRealmModel();
		Objects.requireNonNull(this.userModel);
		Objects.requireNonNull(realmModel);
		return ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, userModel);

	}

	public boolean hasAllRoles(Set<String> roles) {
		Objects.requireNonNull(roles);
		boolean hasRoles = userRoles.containsAll(roles);
		if ( !hasRoles ) {
			throw new ForbiddenException("You do not have the required credentials for this action");
		}
		return hasRoles;
	}

	public void isClient(String clientName) {
		Objects.requireNonNull(clientName);
		String clientId = getClientId();
		Objects.requireNonNull(clientId);
		if ( !clientId.equals(clientName) ) {
			throw new ForbiddenException("You do not have the required credentials for this action");
		}
	}

	public String getClientId() {
		ClientModel clientModel = sessionWrapper.getClientModel();
		Objects.requireNonNull(clientModel);
		return clientModel.getClientId();
	}

	public void shouldAuthenticate() {
		if ( Objects.isNull(sessionWrapper.getAuthResult()) ) {
			throw new NotAuthorizedException("token required");
		}
	}

	public void isTheRightUser(String userId) {
		Objects.requireNonNull(userId);
		if ( !userModel.getId().equals(userId) ) {
			throw new ForbiddenException("You do not have the required credentials for this action");
		}
	}

}

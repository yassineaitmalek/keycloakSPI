package com.test.keyclock.spi.provider;


import com.test.keyclock.spi.resources.GroupsResource;
import com.test.keyclock.spi.resources.RolesResource;
import com.test.keyclock.spi.resources.UsersResource;
import com.test.keyclock.spi.services.GroupService;
import com.test.keyclock.spi.services.RoleService;
import com.test.keyclock.spi.services.UserService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakContext;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakUriInfo;
import org.keycloak.services.resource.RealmResourceProvider;


@RequiredArgsConstructor
public class ResourceProviderSwitcher implements RealmResourceProvider {

	private final KeycloakSession session;

	// provider switcher
	@Override
	public RealmResourceProvider getResource() {
		String subPath = getSubPath();
		switch ( subPath ) {
			case "users":
				return new UsersResource(session, UserService.getInstance());
			case "roles":
				return new RolesResource(session, RoleService.getInstance());
			case "groups":
				return new GroupsResource(session, GroupService.getInstance());
			default:
				throw new RuntimeException("Resource not found: " + subPath);
		}
	}

	@Override
	public void close() {
		// No cleanup needed for wrapper
	}

	private String getSubPath() {
		// /auth/realms/{realm}/{ID}/{subPath}
		return Optional.ofNullable(session).map(KeycloakSession::getContext).map(KeycloakContext::getUri).map(KeycloakUriInfo::getPath).map(e -> e.split("/")).filter(e -> e.length >= 5).map(e -> e[4]).map(String::trim).orElseGet(() -> "");
	}

}

package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import com.test.keyclock.spi.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;


@RequiredArgsConstructor
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Roles Resource", description = "Endpoints powered by Keycloak 9" )
public class RolesResource implements RealmResourceProvider, AbstractResource {

	private static final String SUB_PATH = "/roles";

	private final KeycloakSession session;

	private final RoleService roleService;

	@Override
	public void close() {
		//
	}

	// /auth/realms/{realm}/{ID}/roles
	@Override
	public RealmResourceProvider getResource() {
		return this;
	}

	@GET
	@Path( SUB_PATH )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get all roles", description = "Returns all roles" )
	@ApiResponse( responseCode = "200", description = "Roles retrieved successfully" )
	public Response getAllRoles() {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> roleService.getAll(sessionWrapper));
	}

	@GET
	@Path( SUB_PATH + "/user/{userId}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get roles for a user", description = "Returns roles for a specific user" )
	@ApiResponse( responseCode = "200", description = "User roles retrieved successfully" )
	public Response getUserRoles(@PathParam( "userId" ) String userId) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.isTheRightUser(userId);
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> roleService.getByUserId(sessionWrapper, userId));
	}

	@GET
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get a role by id", description = "Returns a role" )
	@ApiResponse( responseCode = "200", description = "Role retrieved successfully" )
	public Response getRoleById(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> roleService.getById(sessionWrapper, id));
	}

	@DELETE
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "delete a role by id", description = "Deletes a role" )
	@ApiResponse( responseCode = "204", description = "Role deleted successfully" )
	public Response deleteRoleById(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> roleService.deleteById(sessionWrapper, id));
	}

}

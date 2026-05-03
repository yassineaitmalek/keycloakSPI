package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.dto.UserDTO;
import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import com.test.keyclock.spi.services.UserService;
import java.util.HashSet;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;


@RequiredArgsConstructor
public class UsersResource implements RealmResourceProvider, AbstractResource {

	private static final String SUB_PATH = "/users";

	private final KeycloakSession session;

	private final UserService userService;

	@Override
	public void close() {
		//
	}

	// /auth/realms/{realm}/{ID}/users
	@Override
	public RealmResourceProvider getResource() {
		return this;
	}

	@POST
	@Path( SUB_PATH )
	@Consumes( MediaType.APPLICATION_JSON )
	@Produces( MediaType.APPLICATION_JSON )
	public Response createUser(@Valid UserDTO userDTO) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		return created(() -> userService.createUser(sessionWrapper, userDTO));
	}

	@GET
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getUser(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> userService.getById(sessionWrapper, id));
	}

	@GET
	@Path( SUB_PATH + "/{id}/representation" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getUserRepresentation(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> userService.getRepresentationById(sessionWrapper, id));
	}

	@DELETE
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response deleteUser(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> userService.deleteById(sessionWrapper, id));
	}

}

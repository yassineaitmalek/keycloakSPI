package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.GroupService;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
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
public class GroupsResource implements RealmResourceProvider, AbstractResource {

	private static final String SUB_PATH = "/groups";

	private final KeycloakSession session;

	private final GroupService groupService;

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
	public Response getAllGroups() {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> groupService.getAll(sessionWrapper));
	}

	@GET
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getGroupById(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> groupService.getById(sessionWrapper, id));
	}

	@DELETE
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response deleteGroupById(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> groupService.deleteById(sessionWrapper, id));
	}

	@GET
	@Path( SUB_PATH + "/user/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	public Response getUserGroups(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> groupService.getByUserId(sessionWrapper, id));
	}

}

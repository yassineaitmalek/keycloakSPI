package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.GroupService;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.HashSet;
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
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Groups Resource", description = "Endpoints powered by Keycloak 9" )
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
	@Operation( summary = "get all groups", description = "Returns all groups" )
	@ApiResponse( responseCode = "200", description = "Groups retrieved successfully" )
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
	@Operation( summary = "get a group by id", description = "Returns a group" )
	@ApiResponse( responseCode = "200", description = "Group retrieved successfully" )
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
	@Operation( summary = "delete a group by id", description = "Deletes a group" )
	@ApiResponse( responseCode = "204", description = "Group deleted successfully" )
	public Response deleteGroupById(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> groupService.deleteById(sessionWrapper, id));
	}

	@GET
	@Path( SUB_PATH + "/user/{userId}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get groups for a user", description = "Returns groups for a specific user" )
	@ApiResponse( responseCode = "200", description = "User groups retrieved successfully" )
	public Response getUserGroups(@PathParam( "userId" ) String userId) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.isTheRightUser(userId);
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> groupService.getByUserId(sessionWrapper, userId));
	}

	@POST
	@Path( SUB_PATH + "/join/{groupId}/user/{userId}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "join a group", description = "Joins a group" )
	@ApiResponse( responseCode = "204", description = "Group joined successfully" )
	public Response joinGroup(@PathParam( "groupId" ) String groupId, @PathParam( "userId" ) String userId) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> groupService.joinUser(sessionWrapper, groupId, userId));
	}

	@POST
	@Path( SUB_PATH + "/join/{groupId}/user/{userId}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "unjoin a group", description = "Unjoins a group" )
	@ApiResponse( responseCode = "204", description = "Group unjoined successfully" )
	public Response unjoinGroup(@PathParam( "groupId" ) String groupId, @PathParam( "userId" ) String userId) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> groupService.unjoinUser(sessionWrapper, groupId, userId));
	}

}

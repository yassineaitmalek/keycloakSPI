package com.test.keyclock.spi.resources;


import com.test.keyclock.spi.dto.ApiDataResponse;
import com.test.keyclock.spi.dto.UserDTO;
import com.test.keyclock.spi.models.UserDetails;
import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import com.test.keyclock.spi.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement( name = "bearerAuth" )
@Tag( name = "Users Resource", description = "Endpoints powered by Keycloak 9" )
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
	@Operation( summary = "create a user", description = "Returns a created user" )
	@ApiResponse( responseCode = "201", description = "User created successfully" )
	public Response createUser(@Valid UserDTO userDTO) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		return created(() -> userService.createUser(sessionWrapper, userDTO));
	}

	@GET
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get a user by id", description = "Returns a user" )
	@ApiResponse( responseCode = "200", description = "User retrieved successfully", content = @Content( mediaType = MediaType.APPLICATION_JSON, schema = @Schema( implementation = ApiDataResponse.class, subTypes = {UserDetails.class} // Informs Swagger about the inner payload data type
	) ) )
	public Response getUser(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.isTheRightUser(id);
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> userService.getById(sessionWrapper, id));
	}

	@GET
	@Path( SUB_PATH + "/{id}/representation" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "get a user representation by id", description = "Returns a user representation" )
	@ApiResponse( responseCode = "200", description = "User representation retrieved successfully" )
	public Response getUserRepresentation(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.isTheRightUser(id);
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return ok(() -> userService.getRepresentationById(sessionWrapper, id));
	}

	@DELETE
	@Path( SUB_PATH + "/{id}" )
	@Produces( MediaType.APPLICATION_JSON )
	@Operation( summary = "delete a user by id", description = "Deletes a user" )
	@ApiResponse( responseCode = "204", description = "User deleted successfully" )
	public Response deleteUser(@PathParam( "id" ) String id) {
		KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
		SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
		securityCheck.logUser();
		securityCheck.isTheRightUser(id);
		securityCheck.shouldAuthenticate();
		securityCheck.hasAllRoles(new HashSet<>());
		return noContent(() -> userService.deleteById(sessionWrapper, id));
	}

}

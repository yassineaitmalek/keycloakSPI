package com.test.keyclock.spi.resources;


import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;


@RequiredArgsConstructor
public class SwaggerResource implements RealmResourceProvider, AbstractResource {

	private static final String SUB_PATH = "/swagger";

	private static final String TITLE = "Keycloak 9 Extension API";

	private static final String VERSION = "1.0.0";

	private final KeycloakSession session;

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
	@Path( SUB_PATH + "/users" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiUsersSchema() {
		OpenAPI openApi = new OpenAPI();
		openApi.info(new Info().title(TITLE).version(VERSION).description("users endpoints contract"));

		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(openApi);
		return reader.read(UsersResource.class);
	}

	@GET
	@Path( SUB_PATH + "/roles" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiRolesSchema() {
		OpenAPI openApi = new OpenAPI();
		openApi.info(new Info().title(TITLE).version(VERSION).description("roles endpoints contract"));

		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(openApi);
		return reader.read(RolesResource.class);
	}

	@GET
	@Path( SUB_PATH + "/groups" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiGroupsSchema() {
		OpenAPI openApi = new OpenAPI();
		openApi.info(new Info().title(TITLE).version(VERSION).description("groups endpoints contract"));

		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(openApi);
		return reader.read(GroupsResource.class);
	}

}
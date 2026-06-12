package com.test.keyclock.spi.resources;


import io.swagger.v3.jaxrs2.Reader;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

	private static final String SECURITY_SCHEME_NAME = "bearerAuth";

	private static final String SECURITY_SCHEME = "bearer";

	private static final String SECURITY_BEARER_FORMAT = "bearer";

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

	private OpenAPI createOpenApi(String api) {
		OpenAPI openApi = new OpenAPI();
		openApi.info(new Info().title(TITLE).version(VERSION).description(api + " Keycloak 9 Extension API contract")).addSecurityItem(securityRequirement()).components(components());
		return openApi;
	}

	public List<Server> servers() {
		Server server = new Server();
		server.setUrl("/auth/realms/portail-digital/test-multiple-providers");
		return Arrays.asList(server);
	}

	private SecurityRequirement securityRequirement() {
		return new SecurityRequirement().addList(SECURITY_SCHEME_NAME);
	}

	private Components components() {
		return new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme());
	}

	private SecurityScheme securityScheme() {
		return new SecurityScheme().name(SECURITY_SCHEME_NAME).type(SecurityScheme.Type.HTTP).scheme(SECURITY_SCHEME).bearerFormat(SECURITY_BEARER_FORMAT);
	}

	@GET
	@Path( SUB_PATH + "/users" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiUsersSchema() {
		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(createOpenApi("users"));
		return reader.read(UsersResource.class);
	}

	@GET
	@Path( SUB_PATH + "/roles" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiRolesSchema() {
		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(createOpenApi("roles"));
		return reader.read(RolesResource.class);
	}

	@GET
	@Path( SUB_PATH + "/groups" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getOpenApiGroupsSchema() {
		// Legacy Javax Swagger Core JAX-RS reader
		Reader reader = new Reader(createOpenApi("groups"));
		return reader.read(GroupsResource.class);
	}

	/**
	 * Unified single endpoint for all resources
	 * Path: /auth/realms/{realm}/{your-spi-id}/swagger/json
	 */
	@GET
	@Path( SUB_PATH + "/json" )
	@Produces( MediaType.APPLICATION_JSON )
	public OpenAPI getUnifiedOpenApiSchema() {
		// Initialize with the base OpenAPI configuration block
		OpenAPI openApi = createOpenApi("APP");
		Reader reader = new Reader(openApi);

		Set<Class<?>> classes = new HashSet<>(
		        Arrays.asList(UsersResource.class, RolesResource.class, GroupsResource.class));

		// Scan ALL resource classes sequentially to append them to the same definition
		reader.read(classes);

		return reader.getOpenAPI();
	}

}
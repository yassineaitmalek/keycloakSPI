package com.test.keyclock.spi.resources;

import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import com.test.keyclock.spi.services.RoleService;
import java.util.HashSet;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

@RequiredArgsConstructor
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
    @Path(SUB_PATH)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllRoles() {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        return ok(() -> roleService.getAll(sessionWrapper));
    }

    @GET
    @Path(SUB_PATH + "/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserRoles(@PathParam("id") String id) {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        return ok(() -> roleService.getByUserId(sessionWrapper, id));
    }

    @GET
    @Path(SUB_PATH + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoleById(@PathParam("id") String id) {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        return ok(() -> roleService.getById(sessionWrapper, id));
    }

    @DELETE
    @Path(SUB_PATH + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteRoleById(@PathParam("id") String id) {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        return noContent(() -> roleService.deleteById(sessionWrapper, id));
    }
}

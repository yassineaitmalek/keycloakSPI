package com.test.keyclock.spi.resources;

import com.test.keyclock.spi.security.SecurityCheck;
import com.test.keyclock.spi.services.KeycloakSessionWrapper;
import com.test.keyclock.spi.services.RoleService;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.KeycloakSession;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.services.resource.RealmResourceProvider;

@RequiredArgsConstructor
public class RolesResource implements RealmResourceProvider {

    private static final String SUB_PATH = "/roles";

    private final KeycloakSession session;

    private final RoleService roleService;

    @Override
    public void close() {
        //
    }

    // /auth/realms/{realm}/{ID}/roles
    @Override
    public Object getResource() {
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
        Set<RoleRepresentation> roles = roleService.getAll(sessionWrapper);
        return Response.ok(roles).build();
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
        Set<RoleRepresentation> roles = roleService.getByUserId(sessionWrapper, id);
        return Response.ok(roles).build();
    }
}

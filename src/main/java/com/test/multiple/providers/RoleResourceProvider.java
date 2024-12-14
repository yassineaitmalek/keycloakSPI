package com.test.multiple.providers;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import com.test.security.SecurityCheck;
import com.test.services.RoleService;

public class RoleResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public RoleResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {
        //
    }

    // /auth/realms/{realm}/test-multiple-providers/roles
    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/roles/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@PathParam("id") String id) {
        new SecurityCheck(session);
        RoleService roleService = new RoleService(session);
        return Response.ok(roleService.getRoleByUserId(id)).build();
    }

    @GET
    @Path("/roles/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRolesByUsername(@PathParam("username") String username) {
        new SecurityCheck(session);
        RoleService roleService = new RoleService(session);
        return Response.ok(roleService.getRoleByUserName(username)).build();
    }

}
package com.test.single;

import java.util.function.Supplier;

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
import com.test.services.UserService;

public class TestResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public TestResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {
        //
    }

    // /auth/realms/{realm}/test-provider/
    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response hi(@PathParam("realm") String realmName) {
        new SecurityCheck(session, false);
        return Response.ok("hi " + realmName).build();
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {

        UserService userService = new UserService(session);
        return Response.ok(userService.getUserById(id)).build();
    }

    @GET
    @Path("/users/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserByUserName(@PathParam("username") String username) {

        UserService userService = new UserService(session);
        return Response.ok(userService.getUserByUserName(username)).build();
    }

    @GET
    @Path("/users/roles/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRoles(@PathParam("id") String id) {

        RoleService roleService = new RoleService(session);
        return Response.ok(roleService.getRoleByUserId(id)).build();
    }

    @GET
    @Path("/users/username/roles/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRolesByUsername(@PathParam("username") String username) {

        RoleService roleService = new RoleService(session);
        return Response.ok(roleService.getRoleByUserName(username)).build();
    }

}
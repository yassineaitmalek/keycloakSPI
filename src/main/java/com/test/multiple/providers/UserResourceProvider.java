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
import com.test.services.UserService;

public class UserResourceProvider implements RealmResourceProvider {

    private final KeycloakSession session;

    public UserResourceProvider(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void close() {
        //
    }

    // /auth/realms/{realm}/test-multiple-providers/users
    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path("/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        new SecurityCheck(session, true);
        UserService userService = new UserService(session);
        return Response.ok(userService.getUserById(id)).build();
    }

}
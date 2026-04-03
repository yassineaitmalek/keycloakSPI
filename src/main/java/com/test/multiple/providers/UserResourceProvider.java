package com.test.multiple.providers;

import com.test.models.UserDetails;
import com.test.security.SecurityCheck;
import com.test.services.KeycloakSessionWrapper;
import com.test.services.UserService;
import java.util.HashSet;
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
public class UserResourceProvider implements RealmResourceProvider {

    private static final String SUB_PATH = "/users";

    private final KeycloakSession session;

    private final UserService userService;

    @Override
    public void close() {
        //
    }

    // /auth/realms/{realm}/{ID}/users
    @Override
    public Object getResource() {
        return this;
    }

    @GET
    @Path(SUB_PATH + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUser(@PathParam("id") String id) {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        UserDetails user = userService.getById(sessionWrapper, id);
        return Response.ok(user).build();
    }
}

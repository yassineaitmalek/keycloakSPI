package com.test.multiple.providers;

import com.test.security.SecurityCheck;
import com.test.services.GroupService;
import com.test.services.KeycloakSessionWrapper;
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
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.services.resource.RealmResourceProvider;

@RequiredArgsConstructor
public class GroupResourceProvider implements RealmResourceProvider {

    private static final String SUB_PATH = "/groups";

    private final KeycloakSession session;

    private final GroupService groupService;

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
    public Response getAllGroups() {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        Set<GroupRepresentation> groups = groupService.getAll(sessionWrapper);
        return Response.ok(groups).build();
    }

    @GET
    @Path(SUB_PATH + "/user/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserGroups(@PathParam("id") String id) {
        KeycloakSessionWrapper sessionWrapper = new KeycloakSessionWrapper(session);
        SecurityCheck securityCheck = new SecurityCheck(sessionWrapper);
        securityCheck.logUser();
        securityCheck.shouldAuthenticate();
        securityCheck.hasAllRoles(new HashSet<>());
        Set<GroupRepresentation> groups = groupService.getByUserId(sessionWrapper, id);
        return Response.ok(groups).build();
    }
}

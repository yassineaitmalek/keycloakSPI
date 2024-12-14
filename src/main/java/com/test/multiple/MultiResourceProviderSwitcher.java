package com.test.multiple;

import java.util.Arrays;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

import com.test.multiple.providers.RoleResourceProvider;
import com.test.multiple.providers.UserResourceProvider;

public class MultiResourceProviderSwitcher implements RealmResourceProvider {

  private final KeycloakSession session;

  public MultiResourceProviderSwitcher(KeycloakSession session) {
    this.session = session;
  }

  @Override
  public Object getResource() {
    String subPath = getSubPath();
    return switcherToProvider(subPath);

  }

  public Object switcherToProvider(String subPath) {
    switch (subPath) {
      case "users":
        return new UserResourceProvider(session);
      case "roles":
        return new RoleResourceProvider(session);
      default:
        throw new RuntimeException("Resource not found: " + subPath);
    }

  }

  @Override
  public void close() {
    // No cleanup needed for wrapper
  }

  private String getSubPath() {
    // /auth/realms/{realm}/test-multiple-providers/{subPath}
    return Arrays.asList(session.getContext().getUri().getPath().split("/")).get(4).trim();
  }
}

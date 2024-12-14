package com.test.multiple;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class MultiResourceProviderFactory implements RealmResourceProviderFactory {

  public static final String ID = "test-multiple-providers";

  @Override
  public RealmResourceProvider create(KeycloakSession session) {
    return new MultiResourceProviderSwitcher(session);
  }

  @Override
  public void init(Scope config) {
    //
  }

  @Override
  public void postInit(KeycloakSessionFactory factory) {
    // Additional post-initialization logic if needed
  }

  @Override
  public void close() {
    //
  }

  @Override
  public String getId() {
    return ID;
  }

  @Override
  public int order() {
    return 0;
  }

}

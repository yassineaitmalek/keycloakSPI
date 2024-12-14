package com.test.single;

import org.keycloak.Config.Scope;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.services.resource.RealmResourceProvider;
import org.keycloak.services.resource.RealmResourceProviderFactory;

public class TestProviderFactory implements RealmResourceProviderFactory {

	public static final String ID = "test-provider";

	@Override
	public String getId() {
		return ID;
	}

	@Override
	public int order() {
		return 0;
	}

	@Override
	public RealmResourceProvider create(KeycloakSession keycloakSession) {
		return new TestResourceProvider(keycloakSession);
	}

	@Override
	public void init(Scope scope) {
		//
	}

	@Override
	public void postInit(KeycloakSessionFactory keycloakSessionFactory) {
		//
	}

	@Override
	public void close() {
		//
	}
}
package com.test.services;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.keycloak.models.*;
import org.keycloak.services.managers.AppAuthManager;
import org.keycloak.services.managers.AuthenticationManager;

@RequiredArgsConstructor
public class KeycloakSessionWrapper {

    private final KeycloakSession session;

    public KeycloakSession getSession() {
        return session;
    }

    public RealmModel getRealmModel() {
        return getSession().realms().getRealmByName(this.getRealmName());
    }

    public UserProvider getUserProvider() {
        return getSession().users();
    }

    public UserCredentialManager getUserCredentialManager() {
        return getSession().userCredentialManager();
    }

    public String getRealmName() {
        return getSession().getContext().getRealm().getName();
    }

    public AuthenticationManager.AuthResult getAuthResult() {
        return new AppAuthManager().authenticateBearerToken(session);
    }

    public ClientModel getClientModel() {
        AuthenticationManager.AuthResult authResult = getAuthResult();
        RealmModel realmModel = getRealmModel();
        Objects.requireNonNull(authResult);
        Objects.requireNonNull(realmModel);
        return realmModel.getClientByClientId(authResult.getToken().getIssuedFor());
    }
}

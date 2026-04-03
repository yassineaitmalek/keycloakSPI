package com.test.services;

import com.test.dto.RoleDTO;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

public class RoleService {

    private static volatile RoleService instance;

    private RoleService() {}

    public static RoleService getInstance() {
        if (instance == null) {
            synchronized (RoleService.class) {
                if (instance == null) {
                    instance = new RoleService();
                }
            }
        }
        return instance;
    }

    public Set<RoleRepresentation> getAll(KeycloakSessionWrapper sessionWrapper) {
        return sessionWrapper
            .getRealmModel()
            .getRoles()
            .stream()
            .map(ModelToRepresentation::toRepresentation)
            .collect(Collectors.toSet());
    }

    public Set<RoleRepresentation> getByUserId(KeycloakSessionWrapper sessionWrapper, String userId) {
        Objects.requireNonNull(userId);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserModel userModel = userProvider.getUserById(userId, realmModel);
        return Optional
            .ofNullable(userModel)
            .map(UserModel::getRealmRoleMappings)
            .orElseGet(Collections::emptySet)
            .stream()
            .map(ModelToRepresentation::toRepresentation)
            .collect(Collectors.toSet());
    }

    public RoleRepresentation create(KeycloakSessionWrapper sessionWrapper, RoleDTO dto) {
        Objects.requireNonNull(dto, "dto is required");
        Objects.requireNonNull(dto.getName(), "name is required");
        Objects.requireNonNull(dto.getDescription(), "description is required");
        String name = dto.getName().toUpperCase();
        RealmModel realmModel = sessionWrapper.getRealmModel();
        RoleModel existingRole = realmModel.getRole(name);
        if (Objects.nonNull(existingRole)) {
            throw new IllegalStateException("Role already exists: " + name);
        }
        RoleModel role = realmModel.addRole(name);
        role.setDescription(dto.getDescription());
        return ModelToRepresentation.toRepresentation(role);
    }

    public void deleteById(KeycloakSessionWrapper sessionWrapper, String id) {
        Objects.requireNonNull(id);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        RoleModel role = realmModel.getRoleById(id);
        Objects.requireNonNull(role, "Role not found for ID: " + id);
        realmModel.removeRole(role);
    }

    public void revokeFromUser(KeycloakSessionWrapper sessionWrapper, String roleId, String userId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(roleId);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserModel user = userProvider.getUserById(userId, realmModel);
        Objects.requireNonNull(user, "User not found with ID: " + userId);
        RoleModel role = realmModel.getRoleById(roleId);
        Objects.requireNonNull(role, "Role not found for ID: " + roleId);
        if (user.hasRole(role)) {
            user.deleteRoleMapping(role);
        }
    }

    public void grantUser(KeycloakSessionWrapper sessionWrapper, String roleId, String userId) {
        Objects.requireNonNull(userId);
        Objects.requireNonNull(roleId);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserModel user = userProvider.getUserById(userId, realmModel);
        Objects.requireNonNull(user, "User not found with ID: " + userId);
        RoleModel role = realmModel.getRoleById(roleId);
        Objects.requireNonNull(role, "Role not found for ID: " + roleId);
        if (!user.hasRole(role)) {
            user.grantRole(role);
        }
    }
}

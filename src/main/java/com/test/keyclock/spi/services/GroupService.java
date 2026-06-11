package com.test.keyclock.spi.services;


import com.test.keyclock.spi.dto.GroupDTO;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class GroupService {

	private static volatile GroupService instance;

	public static GroupService getInstance() {
		if ( instance == null ) {
			synchronized ( GroupService.class ) {
				if ( instance == null ) {
					instance = new GroupService();
				}
			}
		}
		return instance;
	}

	public Set<GroupRepresentation> getAll(KeycloakSessionWrapper sessionWrapper) {
		RealmModel realmModel = sessionWrapper.getRealmModel();
		return realmModel.getGroups().stream().map(e -> ModelToRepresentation.toRepresentation(e, true)).collect(Collectors.toSet());
	}

	public Set<GroupRepresentation> getByUserId(KeycloakSessionWrapper sessionWrapper, String userId) {
		Objects.requireNonNull(userId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		UserProvider userProvider = sessionWrapper.getUserProvider();
		UserModel user = userProvider.getUserById(userId, realmModel);
		Objects.requireNonNull(user, "User not found with ID: " + userId);
		Set<GroupModel> userGroups = user.getGroups();
		return Optional.ofNullable(userGroups).orElseGet(Collections::emptySet).stream().map(e -> ModelToRepresentation.toRepresentation(e, true)).collect(Collectors.toSet());
	}

	public GroupRepresentation createGroup(KeycloakSessionWrapper sessionWrapper, GroupDTO dto) {
		Objects.requireNonNull(dto, "dto is required");
		Objects.requireNonNull(dto.getName(), "name is required");
		Objects.requireNonNull(dto.getDescription(), "description is required");
		String name = dto.getName().toLowerCase();
		RealmModel realmModel = sessionWrapper.getRealmModel();
		GroupModel groupModel = realmModel.createGroup(name);
		groupModel.setAttribute("description", Arrays.asList(dto.getDescription()));
		return ModelToRepresentation.toRepresentation(groupModel, true);
	}

	public void deleteById(KeycloakSessionWrapper sessionWrapper, String id) {
		Objects.requireNonNull(id);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		GroupModel group = realmModel.getGroupById(id);
		Objects.requireNonNull(group, "Group not found for ID: " + id);
		realmModel.removeGroup(group);
	}

	public GroupRepresentation getById(KeycloakSessionWrapper sessionWrapper, String id) {
		Objects.requireNonNull(id);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		GroupModel groupModel = realmModel.getGroupById(id);
		Objects.requireNonNull(groupModel, "Group not found for ID: " + id);
		return ModelToRepresentation.toRepresentation(groupModel, true);
	}

	public void unjoinUser(KeycloakSessionWrapper sessionWrapper, String groupId, String userId) {
		Objects.requireNonNull(userId);
		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		UserProvider userProvider = sessionWrapper.getUserProvider();
		UserModel user = userProvider.getUserById(userId, realmModel);
		Objects.requireNonNull(user, "User not found with ID: " + userId);
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		if ( user.isMemberOf(group) ) {
			user.leaveGroup(group);
		}
	}

	public void joinUser(KeycloakSessionWrapper sessionWrapper, String groupId, String userId) {
		Objects.requireNonNull(userId);
		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		UserProvider userProvider = sessionWrapper.getUserProvider();
		UserModel user = userProvider.getUserById(userId, realmModel);
		Objects.requireNonNull(user, "User not found with ID: " + userId);
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		if ( !user.isMemberOf(group) ) {
			user.joinGroup(group);
		}
	}

	public void assignRole(KeycloakSessionWrapper sessionWrapper, String groupId, String roleId) {
		Objects.requireNonNull(roleId);
		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		RoleModel role = realmModel.getRoleById(roleId);
		Objects.requireNonNull(role, "Role not found with ID: " + roleId);
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		if ( !group.hasRole(role) ) {
			group.grantRole(role);
		}
	}

	public void revokeRole(KeycloakSessionWrapper sessionWrapper, String groupId, String roleId) {
		Objects.requireNonNull(roleId);
		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		RoleModel role = realmModel.getRoleById(roleId);
		Objects.requireNonNull(role, "Role not found with ID: " + roleId);
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		if ( group.hasRole(role) ) {
			group.deleteRoleMapping(role);
		}
	}

	public Set<RoleRepresentation> getGroupRoles(KeycloakSessionWrapper sessionWrapper, String groupId) {

		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		return Optional.ofNullable(group.getRoleMappings()).orElseGet(Collections::emptySet).stream().map(ModelToRepresentation::toRepresentation).collect(Collectors.toSet());
	}

}

package com.test.services;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.GroupRepresentation;

import com.test.dto.GroupDTO;

public class GroupService {

  private final KeycloakSession session;

  private final RealmModel realmModel;

  private final UserProvider userProvider;

  private final String realmName;

  public GroupService(KeycloakSession session) {
    this.session = session;
    this.userProvider = session.users();
    this.realmName = session.getContext().getRealm().getName();
    this.realmModel = session.realms().getRealmByName(this.realmName);

  }

  public Set<GroupRepresentation> getGroups() {

    return realmModel.getGroups()
        .stream()
        .map(e -> ModelToRepresentation.toRepresentation(e, true))
        .collect(Collectors.toSet());

  }

  public Set<GroupRepresentation> getGroupsByUserId(String id) {
    return Optional.ofNullable(id)
        .map(e -> userProvider.getUserById(e, realmModel))
        .map(this::getGroupsByUserModel)
        .orElseGet(Collections::emptySet);

  }

  public Set<GroupRepresentation> getGroupsByUserModel(UserModel userModel) {
    return Optional.ofNullable(userModel)
        .map(UserModel::getGroups)
        .orElseGet(Collections::emptySet)
        .stream()
        .map(e -> ModelToRepresentation.toRepresentation(e, true))
        .collect(Collectors.toSet());

  }

  public Set<GroupModel> getGroups(Set<String> groups) {
    return Optional.ofNullable(groups)
        .orElseGet(Collections::emptySet)
        .stream()
        .map(realmModel::getGroupById)
        .collect(Collectors.toSet());
  }

  public UserModel joinGroups(UserModel userModel, Set<String> groups) {
    getGroups(groups).stream().forEach(userModel::joinGroup);
    return userModel;
  }

  public GroupRepresentation createGroup(GroupDTO groupDTO) {

    if (Objects
        .nonNull(getGroups().stream().filter(e -> e.getName().equals(groupDTO.getName())).findFirst().orElse(null))) {
      throw new IllegalStateException("Group already exists: " + groupDTO.getName());
    }
    GroupModel groupModel = realmModel.createGroup(groupDTO.getName());

    return ModelToRepresentation.toRepresentation(groupModel, true);
  }

  public void deleteGroupById(String groupId) {

    GroupModel group = realmModel.getGroupById(groupId);
    if (Objects.isNull(group)) {
      throw new IllegalStateException("Group not found for ID: " + groupId);
    }
    realmModel.removeGroup(group);

  }

  public void removeGroupFromUser(String userId, String groupId) {

    UserModel user = userProvider.getUserById(userId, realmModel);

    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("User not found with ID: " + userId);
    }

    GroupModel group = realmModel.getGroupById(groupId);
    if (Objects.isNull(group)) {
      throw new IllegalArgumentException("Group not found: " + groupId);
    }

    if (user.isMemberOf(group)) {
      user.leaveGroup(group);

    }
  }

  public void addGroupFromUser(String userId, String groupId) {

    UserModel user = userProvider.getUserById(userId, realmModel);

    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("User not found with ID: " + userId);
    }

    GroupModel group = realmModel.getGroupById(groupId);
    if (Objects.isNull(group)) {
      throw new IllegalArgumentException("Group not found: " + groupId);
    }

    if (!user.isMemberOf(group)) {
      user.joinGroup(group);

    }
  }

}

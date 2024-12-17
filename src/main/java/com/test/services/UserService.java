package com.test.services;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.keycloak.credential.CredentialModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialManager;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.models.utils.ModelToRepresentation;

import com.test.dto.UserDTO;
import com.test.models.UserDetails;
import com.test.models.constants.Gender;
import com.test.security.SecurityCheck;

public class UserService {

  private final KeycloakSession session;

  private final RealmModel realmModel;

  private final UserProvider userProvider;

  private final UserCredentialManager userCredentialManager;

  private final String realmName;

  private final RoleService roleService;

  private final GroupService groupService;

  public UserService(KeycloakSession session) {
    this.session = session;
    this.userProvider = session.users();
    this.realmName = session.getContext().getRealm().getName();
    this.realmModel = session.realms().getRealmByName(this.realmName);
    this.userCredentialManager = session.userCredentialManager();
    this.roleService = new RoleService(session);
    this.groupService = new GroupService(session);
  }

  public UserDetails getUserById(String id) {
    return Optional.ofNullable(id)
        .map(e -> userProvider.getUserById(e, realmModel))
        .map(this::convert)
        .orElseThrow(() -> new RuntimeException("user not found"));

  }

  public UserDetails getCurrUser(SecurityCheck security) {

    return convert(security.getCurrentUser());

  }

  public void deleteUserById(String id) {

    UserModel user = userProvider.getUserById(id, realmModel);
    if (Objects.isNull(user)) {
      throw new IllegalArgumentException("User not found with ID: " + id);
    }

    userProvider.removeUser(realmModel, user);

  }

  public UserDetails createUser(UserDTO userDTO) {

    UserModel user = userProvider.addUser(realmModel, userDTO.getUsername());
    user.setEnabled(true);
    user.setEmail(userDTO.getEmail());
    user.setFirstName(userDTO.getFirstName());
    user.setLastName(userDTO.getLastName());

    user.setAttribute("mobile", Arrays.asList(userDTO.getMobile()));
    user.setAttribute("birthDate", Arrays.asList(userDTO.getBirthDate().toString()));
    user.setAttribute("gender", Arrays.asList(userDTO.getGender().toString()));

    CredentialModel credentialModel = new CredentialModel();
    credentialModel.setType(CredentialModel.PASSWORD);
    credentialModel.setCreatedDate(System.currentTimeMillis());
    credentialModel.setSecretData(userDTO.getPassword());

    PasswordCredentialModel passwordCredentialModel = PasswordCredentialModel
        .createFromCredentialModel(credentialModel);
    userCredentialManager.updateCredential(realmModel, user, passwordCredentialModel);

    roleService.grantRoles(user, userDTO.getRoles());
    groupService.joinGroups(user, userDTO.getGroups());

    return convert(user);

  }

  public UserDetails convert(UserModel userModel) {

    return UserDetails.builder()
        .userRepresentation(ModelToRepresentation.toRepresentation(session, realmModel, userModel))
        .roleRepresentations(roleService.getRoleByUserModel(userModel))
        .groupRepresentations(groupService.getGroupsByUserModel(userModel))
        .mobile(userModel.getFirstAttribute("mobile"))
        .birthDate(LocalDate.parse(userModel.getFirstAttribute("birthDate")))
        .gender(Gender.of(userModel.getFirstAttribute("gender")))
        .build();
  }

  public List<UserDetails> getAllUsersInGroup(String groupId) {

    GroupModel group = realmModel.getGroupById(groupId);
    if (Objects.isNull(group)) {
      throw new IllegalArgumentException("Group not found: " + groupId);
    }

    return userProvider.getGroupMembers(realmModel, group)
        .stream()
        .map(this::convert)
        .collect(Collectors.toList());
  }

  public List<UserDetails> getAllUsersInRole(String roleId) {

    RoleModel role = realmModel.getRoleById(roleId);
    if (Objects.isNull(role)) {
      throw new IllegalArgumentException("Group not found: " + roleId);
    }
    return userProvider.getRoleMembers(realmModel, role)
        .stream()
        .map(this::convert)
        .collect(Collectors.toList());
  }

}

package com.test.services;

import com.test.dto.UserDTO;
import com.test.models.UserDetails;
import com.test.security.SecurityCheck;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.*;
import org.keycloak.models.credential.PasswordCredentialModel;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

public class UserService {

    private static volatile UserService instance;

    private UserService() {}

    public static UserService getInstance() {
        if (instance == null) {
            synchronized (UserService.class) {
                if (instance == null) {
                    instance = new UserService();
                }
            }
        }
        return instance;
    }

    public UserDetails getById(KeycloakSessionWrapper sessionWrapper, String id) {
        Objects.requireNonNull(id);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserModel userModel = userProvider.getUserById(id, realmModel);

        UserRepresentation userRepresentation = ModelToRepresentation.toRepresentation(
            sessionWrapper.getSession(),
            realmModel,
            userModel
        );

        return convert(userRepresentation);
    }

    public UserDetails getCurrentUser(SecurityCheck security) {
        return convert(security.getCurrentUser());
    }

    public void deleteById(KeycloakSessionWrapper sessionWrapper, String id) {
        Objects.requireNonNull(id);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserModel user = userProvider.getUserById(id, realmModel);
        Objects.requireNonNull(user, "User not found with ID: " + id);
        userProvider.removeUser(realmModel, user);
    }

    public UserDetails createUser(KeycloakSessionWrapper sessionWrapper, UserDTO userDTO) {
        Objects.requireNonNull(userDTO);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        UserCredentialManager userCredentialManager = sessionWrapper.getUserCredentialManager();

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

        PasswordCredentialModel passwordCredentialModel = PasswordCredentialModel.createFromCredentialModel(
            credentialModel
        );
        userCredentialManager.updateCredential(realmModel, user, passwordCredentialModel);

        UserRepresentation userRepresentation = ModelToRepresentation.toRepresentation(
            sessionWrapper.getSession(),
            realmModel,
            user
        );

        return convert(userRepresentation);
    }

    public UserDetails convert(UserRepresentation userRepresentation) {
        return UserDetails
            .builder()
            .userRepresentation(userRepresentation)
            //        .mobile(userModel.getFirstAttribute("mobile"))
            //        .birthDate(LocalDate.parse(userModel.getFirstAttribute("birthDate")))
            //        .gender(Gender.of(userModel.getFirstAttribute("gender")))
            .build();
    }

    public List<UserDetails> getAllUsersInGroup(KeycloakSessionWrapper sessionWrapper, String groupId) {
        Objects.requireNonNull(groupId);
        RealmModel realmModel = sessionWrapper.getRealmModel();
        UserProvider userProvider = sessionWrapper.getUserProvider();
        GroupModel group = realmModel.getGroupById(groupId);
        Objects.requireNonNull(group, "Group not found for ID: " + groupId);
        return userProvider
            .getGroupMembers(realmModel, group)
            .stream()
            .map(e -> ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, e))
            .map(this::convert)
            .collect(Collectors.toList());
    }
}

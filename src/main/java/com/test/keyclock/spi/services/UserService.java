package com.test.keyclock.spi.services;


import com.test.keyclock.spi.dto.UserDTO;
import com.test.keyclock.spi.mappers.UserMapper;
import com.test.keyclock.spi.models.UserDetails;
import com.test.keyclock.spi.security.SecurityCheck;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.keycloak.models.GroupModel;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserCredentialManager;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;
import org.keycloak.models.UserProvider;
import org.keycloak.models.utils.ModelToRepresentation;
import org.keycloak.representations.idm.UserRepresentation;


@NoArgsConstructor( access = AccessLevel.PRIVATE )
public class UserService {

	private static volatile UserService instance;

	public static UserService getInstance() {
		if ( instance == null ) {
			synchronized ( UserService.class ) {
				if ( instance == null ) {
					instance = new UserService();
				}
			}
		}
		return instance;
	}

	public UserDetails getById(KeycloakSessionWrapper sessionWrapper, String id) {
		UserRepresentation userRepresentation = getRepresentationById(sessionWrapper, id);
		return UserMapper.toUserDetails(userRepresentation);
	}

	public UserRepresentation getRepresentationById(KeycloakSessionWrapper sessionWrapper, String id) {
		Objects.requireNonNull(id, "User ID cannot be null");
		RealmModel realmModel = sessionWrapper.getRealmModel();
		UserProvider userProvider = sessionWrapper.getUserProvider();
		UserModel userModel = userProvider.getUserById(id, realmModel);
		return ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, userModel);

	}

	public UserDetails getCurrentUser(SecurityCheck security) {

		return UserMapper.toUserDetails(security.getCurrentUserRepresentation());
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

		UserMapper.getAttributes(userDTO).forEach(user::setSingleAttribute);

		userCredentialManager.updateCredential(
		        realmModel, user, UserCredentialModel.password(userDTO.getPassword(), false));

		UserRepresentation userRepresentation = ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, user);

		return UserMapper.toUserDetails(userRepresentation);
	}

	public List<UserDetails> getAllUsersInGroup(KeycloakSessionWrapper sessionWrapper, String groupId) {
		Objects.requireNonNull(groupId);
		RealmModel realmModel = sessionWrapper.getRealmModel();
		UserProvider userProvider = sessionWrapper.getUserProvider();
		GroupModel group = realmModel.getGroupById(groupId);
		Objects.requireNonNull(group, "Group not found for ID: " + groupId);
		return userProvider.getGroupMembers(realmModel, group).stream().map(userModel -> ModelToRepresentation.toRepresentation(sessionWrapper.getSession(), realmModel, userModel)).map(UserMapper::toUserDetails).collect(Collectors.toList());
	}

}

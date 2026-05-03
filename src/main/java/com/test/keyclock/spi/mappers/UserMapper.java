package com.test.keyclock.spi.mappers;


import com.test.keyclock.spi.dto.UserDTO;
import com.test.keyclock.spi.models.UserDetails;
import com.test.keyclock.spi.models.constants.Gender;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.UtilityClass;
import org.keycloak.representations.idm.UserRepresentation;


@UtilityClass
public class UserMapper {

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	private static final String MOBILE = "mobile";

	private static final String GENDER = "gender";

	private static final String PHOTO = "photo";

	private static final String MATRICULE = "matricule";

	private static final String IDENTIFIANT = "identifiant";

	private static final String BIRTH_DATE = "birthDate";

	private static final String DATE_DEBUT = "dateDebut";

	private static final String DATE_FIN = "dateFin";

	public Map<String, String> getAttributes(UserDTO userDTO) {

		Objects.requireNonNull(userDTO);
		Map<String, String> attributes = new HashMap<>();
		attributes.put(MOBILE, userDTO.getMobile());
		attributes.put(GENDER, Objects.nonNull(userDTO.getGender()) ? userDTO.getGender().getValue() : null);
		attributes.put(PHOTO, userDTO.getPhoto());
		attributes.put(MATRICULE, userDTO.getMatricule());
		attributes.put(IDENTIFIANT, userDTO.getIdentifiant());
		attributes.put(BIRTH_DATE, formatDate(userDTO.getBirthDate()));
		attributes.put(DATE_DEBUT, formatDate(userDTO.getDateDebut()));
		attributes.put(DATE_FIN, formatDate(userDTO.getDateFin()));
		return attributes;
	}

	public UserDetails toUserDetails(UserRepresentation userRepresentation) {
		Objects.requireNonNull(userRepresentation, "UserRepresentation cannot be null");

		Map<String, String> attributes = userRepresentation.getAttributes().entrySet().stream().collect(
		        Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().isEmpty() ? null : entry.getValue().get(0)));

		return UserDetails.builder().id(userRepresentation.getId()).createdTimestamp(LocalDateTime.ofInstant(
		        Instant.ofEpochMilli(userRepresentation.getCreatedTimestamp()), ZoneId.systemDefault()).toString()).username(userRepresentation.getUsername()).enabled(userRepresentation.isEnabled()).totp(userRepresentation.isTotp()).emailVerified(userRepresentation.isEmailVerified()).firstName(userRepresentation.getFirstName()).lastName(userRepresentation.getLastName()).email(userRepresentation.getEmail()).mobile(attributes.get(MOBILE)).gender(Gender.of(attributes.get(GENDER))).photo(attributes.get(PHOTO)).matricule(attributes.get(MATRICULE)).identifiant(attributes.get(IDENTIFIANT)).birthDate(parseDate(attributes.get(BIRTH_DATE))).dateDebut(parseDate(attributes.get(DATE_DEBUT))).dateFin(parseDate(attributes.get(DATE_FIN))).build();
	}

	private LocalDate parseDate(String dateStr) {
		try {
			return LocalDate.parse(dateStr);
		} catch ( Exception e ) {
			return null;
		}
	}

	private String formatDate(LocalDate date) {
		if ( Objects.isNull(date) ) {
			return null;
		}
		return date.format(DateTimeFormatter.ofPattern(DATE_PATTERN));
	}

}

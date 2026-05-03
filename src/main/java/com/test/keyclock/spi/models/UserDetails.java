package com.test.keyclock.spi.models;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.test.keyclock.spi.models.constants.Gender;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

	private String id;

	private String createdTimestamp;

	private String username;

	private boolean enabled;

	private boolean totp;

	private boolean emailVerified;

	private String firstName;

	private String lastName;

	private String email;

	private String mobile;

	private Gender gender;

	private String photo;

	private String matricule;

	private String identifiant;

	@JsonFormat( pattern = "yyyy-MM-dd" )
	private LocalDate birthDate;

	@JsonFormat( pattern = "yyyy-MM-dd" )
	private LocalDate dateDebut;

	@JsonFormat( pattern = "yyyy-MM-dd" )
	private LocalDate dateFin;

}

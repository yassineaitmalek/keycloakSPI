package com.test.keyclock.spi.dto;


import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

	@NotEmpty( message = "Username is required" )
	private String username;

	@NotEmpty( message = "Password is required" )
	private String password;

	@NotEmpty( message = "First name is required" )
	private String firstName;

	@NotEmpty( message = "Last name is required" )
	private String lastName;

	@NotEmpty( message = "Email is required" )
	private String email;

	@NotEmpty( message = "Mobile is required" )
	private String mobile;

	@NotEmpty( message = "Gender is required" )
	private String gender;

	@NotEmpty( message = "Photo is required" )
	private String photo;

	@NotEmpty( message = "Matricule is required" )
	private String matricule;

	@NotEmpty( message = "Identifiant is required" )
	private String identifiant;

	@NotEmpty( message = "Birth date is required" )
	private String birthDate;

	@NotEmpty( message = "Date debut is required" )
	private String dateDebut;

	private String dateFin;

}

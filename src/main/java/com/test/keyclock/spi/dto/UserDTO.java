package com.test.keyclock.spi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema( name = "UserDTO", description = "DTO representing a custom user payload for creation" )
public class UserDTO {

	@Schema( description = "Username", required = true )
	@NotEmpty( message = "Username is required" )
	private String username;

	@Schema( description = "Password", required = true )
	@NotEmpty( message = "Password is required" )
	private String password;

	@Schema( description = "First name", required = true )
	@NotEmpty( message = "First name is required" )
	private String firstName;

	@Schema( description = "Last name", required = true )
	@NotEmpty( message = "Last name is required" )
	private String lastName;

	@Schema( description = "Email address", required = true )
	@NotEmpty( message = "Email is required" )
	private String email;

	@Schema( description = "Mobile number", required = true )
	@NotEmpty( message = "Mobile is required" )
	private String mobile;

	@Schema( description = "Gender", required = true )
	@NotEmpty( message = "Gender is required" )
	private String gender;

	@Schema( description = "Photo URL", required = false )
	private String photo;

	@Schema( description = "Matricule", required = true )
	@NotEmpty( message = "Matricule is required" )
	private String matricule;

	@Schema( description = "Identifiant", required = true )
	@NotEmpty( message = "Identifiant is required" )
	private String identifiant;

	@Schema( description = "Birth date", required = true )
	@NotEmpty( message = "Birth date is required" )
	private String birthDate;

	@Schema( description = "Start date", required = true )
	@NotEmpty( message = "Date debut is required" )
	private String dateDebut;

	@Schema( description = "End date" )
	private String dateFin;

}

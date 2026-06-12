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
@Schema( name = "RoleDTO", description = "DTO representing a custom role payload for creation" )
public class RoleDTO {

	@Schema( description = "Role name", required = true )
	@NotEmpty( message = "Role name is required" )
	private String name;

	@Schema( description = "Role description", required = false )
	@NotEmpty( message = "Role description is required" )
	private String description;

}

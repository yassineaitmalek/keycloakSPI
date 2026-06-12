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
@Schema( name = "GroupDTO", description = "DTO representing a custom group payload for creation" )
public class GroupDTO {

	@Schema( description = "Group name", required = true )
	@NotEmpty( message = "Group name is required" )
	private String name;

	@Schema( description = "Group description", required = false )
	@NotEmpty( message = "Group description is required" )
	private String description;

}

package com.test.keyclock.spi.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema( name = "ApiDataResponse", description = "Generic API response wrapper for successful responses" )
public class ApiDataResponse<T> {

	@Schema( description = "Response status" )
	private String status;

	@Schema( description = "HTTP status code" )
	private Integer httpStatus;

	@Builder.Default
	@Schema( description = "Date" )
	private String date = LocalDate.now().toString();

	@Builder.Default
	@Schema( description = "Time" )
	private String time = LocalTime.now().toString();

	@Builder.Default
	@Schema( description = "Time zone" )
	private String zone = ZoneId.systemDefault().toString();

	@Schema( description = "Response data" )
	private T data;

}

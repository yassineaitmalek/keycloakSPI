package com.test.keyclock.spi.models.constants;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public enum Gender {
	@JsonProperty( "M" ) MALE("M"),

	@JsonProperty( "F" ) FEMALE("F"),

	@JsonProperty( "NAN" ) NAN("NAN");

	private final String value;

	public static Gender of(String value) {
		return Stream.of(values()).filter(e -> !NAN.equals(e)).filter(e -> e.getValue().equals(value)).findFirst().orElseGet(() -> NAN);
	}

}

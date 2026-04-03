package com.test.keyclock.spi.models.constants;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    @JsonProperty("Male")
    MALE("M"),

    @JsonProperty("Female")
    FEMALE("F"),

    @JsonProperty("NAN")
    NAN("NAN");

    private final String value;

    public static Gender of(String value) {
        return Arrays
            .asList(values())
            .stream()
            .filter(e -> !e.equals(NAN))
            .filter(e -> e.getValue().equals(value))
            .findFirst()
            .orElse(NAN);
    }
}

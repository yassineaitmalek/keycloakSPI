package com.test.models.constants;

import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    return Arrays.asList(values())
        .stream()
        .filter(e -> !e.equals(NAN))
        .filter(e -> e.getValue().equals(value))
        .findFirst()
        .orElse(NAN);

  }
}

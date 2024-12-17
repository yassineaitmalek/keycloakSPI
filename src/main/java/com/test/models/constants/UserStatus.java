package com.test.models.constants;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

  @JsonProperty("New")
  NEW("New"),

  @JsonProperty("Active")
  ACTIVE("Active"),

  @JsonProperty("Banned")
  BANNED("Banned");

  private final String value;

}

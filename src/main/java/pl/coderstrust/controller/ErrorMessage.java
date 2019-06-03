package pl.coderstrust.controller;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorMessage {
  private final String message;


  @JsonCreator
  public ErrorMessage(@JsonProperty("message") String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

package com.perfexiolegal.billingsystem.Exceptions;

public class JsonException extends Exception {
  public JsonException() {
  }

  public JsonException(String message) {
    super(message);
  }

  public JsonException(String message, Throwable cause) {
    super(message, cause);
  }
}


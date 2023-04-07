package com.devlach.vertx_starter.customcodec;

public class Ping {

  private final String message;
  private final boolean isPing;

  public Ping(String message, boolean isPing) {
    this.message = message;
    this.isPing = isPing;
  }

  public String getMessage() {
    return message;
  }

  public boolean isPing() {
    return isPing;
  }

  @Override
  public String toString() {
    return "Ping{" +
      "message='" + message + '\'' +
      ", isPing=" + isPing +
      '}';
  }
}

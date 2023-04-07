package com.devlach.vertx_starter.customcodec;

public class Pong {
  private final Integer id;

  public Pong(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return id;
  }

  @Override
  public String toString() {
    return "Pong{" +
      "id=" + id +
      '}';
  }
}

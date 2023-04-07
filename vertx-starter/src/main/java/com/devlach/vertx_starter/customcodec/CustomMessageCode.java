package com.devlach.vertx_starter.customcodec;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class CustomMessageCode<T> implements MessageCodec<T, T> {

  private final Class<T> clazz;

  public CustomMessageCode(Class<T> clazz) {
    this.clazz = clazz;
  }
  @Override
  public void encodeToWire(Buffer buffer, T t) {
     throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public T decodeFromWire(int pos, Buffer buffer) {
    throw new UnsupportedOperationException("Not implemented");
  }

  @Override
  public T transform(final T t) {
    return t;
  }

  @Override
  public String name() {
    return clazz.getName();
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }
}

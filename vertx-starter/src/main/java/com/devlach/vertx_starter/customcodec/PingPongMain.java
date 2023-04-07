package com.devlach.vertx_starter.customcodec;

import io.vertx.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PingPongMain {

  private static final Logger logger = LogManager.getLogger(PingPongMain.class);
  public static final String REQUEST_ADDRESS = "ping.pong.address";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new PingVerticle(), logOnErrorDeployVerticle(PingVerticle.class));
    vertx.deployVerticle(new PongVerticle(), logOnErrorDeployVerticle(PongVerticle.class));
  }

  private static Handler<AsyncResult<String>> logOnErrorDeployVerticle(Class<?> clazz) {
    return replay -> {
      if (replay.failed()) {
        logger.error("Failed to deploy: {}", clazz.getName(), replay.cause());
      }
    };
  }

  static class PingVerticle extends AbstractVerticle {

    private static final Logger logger = LogManager.getLogger(PingVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      final Ping ping = new Ping("Hello, I'm here!!", true);
      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Ping.class, new CustomMessageCode<>(Ping.class));
      eventBus.<Pong>request(REQUEST_ADDRESS, ping, replay -> logger.debug("Received response: {}", replay.result().body()));
      startPromise.complete();
    }
  }

  static class PongVerticle extends AbstractVerticle {

    private static final Logger logger = LogManager.getLogger(PongVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      var eventBus = vertx.eventBus();
      eventBus.registerDefaultCodec(Pong.class, new CustomMessageCode<>(Pong.class));
      eventBus.<Ping>consumer(
        REQUEST_ADDRESS,
        message -> {
          logger.debug("Received message: {}", message.body());
          message.reply(new Pong(0));
        }
      );
    }
  }
}


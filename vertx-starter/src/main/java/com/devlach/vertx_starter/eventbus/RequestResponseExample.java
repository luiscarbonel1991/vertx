package com.devlach.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RequestResponseExample {

  public static final String REQUEST_ADDRESS = "request.adress";

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new RequestVerticle());
    vertx.deployVerticle(new ResponseVerticle());
  }

  static class RequestVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(RequestVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      EventBus eventBus = vertx.eventBus();
      String message = "Hi, I'm here!!";
      logger.debug("Sending message: {}", message);
      eventBus.request(REQUEST_ADDRESS, message, replay -> {
        logger.debug("Received response: {}", replay.result().body());
      });
    }
  }

  static class ResponseVerticle extends AbstractVerticle {

    private static final Logger logger = LogManager.getLogger(ResponseVerticle.class);

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(
        REQUEST_ADDRESS,
        message -> {
          logger.debug("Received message: {}", message.body());
          message.reply("Hello, I'm here too!!");
        }
      );
    }
  }
}

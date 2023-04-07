package com.devlach.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class PointToPointMain {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new SenderVerticle());
    vertx.deployVerticle(new ReceiverVerticle());
  }


  static class SenderVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();

      vertx.setPeriodic(1500, id -> {
        vertx.eventBus().send(SenderVerticle.class.getName(), String.format("Sending message [%s]", id + new Random().nextInt(1000)));
      });
    }
  }

  static class ReceiverVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(ReceiverVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(SenderVerticle.class.getName(), message -> {
        logger.debug("Received message: {}", message.body());
      });
    }
  }
}

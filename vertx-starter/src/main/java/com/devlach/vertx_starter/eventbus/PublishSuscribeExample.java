package com.devlach.vertx_starter.eventbus;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Random;

public class PublishSuscribeExample {

  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new PublisherVerticle());
    vertx.deployVerticle(new SubscriberVerticle());
    vertx.deployVerticle(new SuscriberVerticle2());
  }


  static class PublisherVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();

      vertx.setPeriodic(Duration.ofSeconds(10).toMillis(), id -> {
        vertx.eventBus().publish(PublisherVerticle.class.getName(), String.format("Sending message [%s]", id + new Random().nextInt(1000)));
      });
    }
  }

  static class SubscriberVerticle extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(SubscriberVerticle.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(PublisherVerticle.class.getName(), message -> {
        logger.debug("Subscriber {}. Received message: {}", getClass().getName(), message.body());
      });
    }
  }

  static class SuscriberVerticle2 extends AbstractVerticle {
    private static final Logger logger = LogManager.getLogger(SuscriberVerticle2.class);
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
      startPromise.complete();
      vertx.eventBus().consumer(PublisherVerticle.class.getName(), message -> {
        logger.debug("Subscriber {}. Received message: {}", getClass().getName(), message.body());
      });
    }
  }
}

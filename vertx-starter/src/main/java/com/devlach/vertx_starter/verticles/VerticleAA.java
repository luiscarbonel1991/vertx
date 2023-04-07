package com.devlach.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleAA extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(VerticleAA.class);
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Start {} verticle", getClass().getName());
    startPromise.complete();
  }

  @Override
  public void stop(Promise<Void> stopPromise) throws Exception {
    logger.debug("Stop {}", getClass().getName());
    stopPromise.complete();
  }
}

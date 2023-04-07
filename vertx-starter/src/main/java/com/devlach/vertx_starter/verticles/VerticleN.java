package com.devlach.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleN extends AbstractVerticle {
  private static final Logger logger = LogManager.getLogger(VerticleAA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Start {} verticle on threah {} with config {}",
      getClass().getName(),
      Thread.currentThread().getName(),
      config());
    startPromise.complete();
  }
}

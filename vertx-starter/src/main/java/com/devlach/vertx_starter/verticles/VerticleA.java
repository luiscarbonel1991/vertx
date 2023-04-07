package com.devlach.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VerticleA extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(VerticleA.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Start {} verticle", getClass().getName());
    vertx.deployVerticle(new VerticleAA(), whenDeployed -> {
      logger.debug("Deployed = {}", VerticleAA.class.getName());
      vertx.undeploy(whenDeployed.result());
    });
    vertx.deployVerticle(new VerticleAB(), whenDeployed -> {
      logger.debug("Deployed = {}", VerticleAB.class.getName());
    });
    startPromise.complete();
  }
}

package com.devlach.vertx_starter.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(MainVerticle.class);
  private static final int INSTANCES_NUMBER = 4;

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Start {} verticle", getClass().getName());
    vertx.deployVerticle(new VerticleA());
    vertx.deployVerticle(new VerticleB());
    vertx.deployVerticle(VerticleN.class.getName(),
      new DeploymentOptions()
        .setInstances(INSTANCES_NUMBER)
        .setConfig(new JsonObject()
          .put("id", UUID.randomUUID().toString())
          .put("name", VerticleN.class.getSimpleName())));
    startPromise.complete();
  }
}

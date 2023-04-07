package com.devlach.vertx_web;

import io.vertx.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MainVerticle extends AbstractVerticle {


  private static final Logger logger = LogManager.getLogger(MainVerticle.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle())
      .onSuccess(id -> {
        logger.info("Deployed {} with id {}", MainVerticle.class.getName(), id);
      })
      .onFailure(err -> {
        logger.error("MainVerticle deployment failed", err);
      });

    vertx.exceptionHandler(err -> {
      logger.error("Unhandled exception", err);
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    vertx.deployVerticle(VersionInfoVerticle.class.getName())
      .onFailure(err -> logger.error("VersionInfoVerticle deployment failed", err))
      .onSuccess(id -> logger.info("Deployed {} with id {}", VersionInfoVerticle.class.getSimpleName(), id))
      .compose(id -> deployRestApiVerticle(startPromise));

  }

  private Future<String> deployRestApiVerticle(Promise<Void> startPromise) {
    return vertx.deployVerticle(RestApiVerticle.class.getName(),
        new DeploymentOptions().setInstances(getInstances()))
      .onSuccess(id -> {
        logger.info("Deployed {} with id {}", RestApiVerticle.class.getSimpleName(), id);
        startPromise.complete();
      })
      .onFailure(err -> {
        logger.error("RestApiVerticle deployment failed", err);
        startPromise.fail(err);
      });
  }

  private int getInstances() {
    return Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
  }


}

package com.devlach.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerMain extends AbstractVerticle {


  private static final Logger logger = LogManager.getLogger(WorkerMain.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new WorkerMain());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(
      new WorkerVerticle(),
      new DeploymentOptions()
        .setWorker(true)
        .setWorkerPoolSize(1)
        .setWorkerPoolName("my-worker-verticle-pool")
    );

    startPromise.complete();
    vertx.executeBlocking(event -> {
      logger.debug("Executing blocking code");
      try {
        Thread.sleep(3000);
        //event.fail("Ohh!! Something wrong!!");
        event.complete();
      } catch (InterruptedException e) {
        logger.error("Failed execution: {}", e.getMessage());
        event.fail(e);
      }
    }, result -> {
      if (result.succeeded()) {
        logger.debug("Blocking execution was good!!!");
      } else {
        logger.debug("Blocking execution failed: ", result.cause());
      }
    });
  }
}

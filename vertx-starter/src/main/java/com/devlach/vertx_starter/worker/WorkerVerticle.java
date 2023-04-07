package com.devlach.vertx_starter.worker;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WorkerVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(WorkerVerticle.class);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Deployed worker verticle {}", getClass().getName());
    startPromise.complete();
    Thread.sleep(3000);
    logger.debug("Blocking execution is done!!");
  }
}

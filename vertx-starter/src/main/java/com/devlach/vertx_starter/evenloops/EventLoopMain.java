package com.devlach.vertx_starter.evenloops;

import io.vertx.core.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

public class EventLoopMain extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(EventLoopMain.class);
  public static void main(String[] args) {
    var vertx = Vertx.vertx(
      new VertxOptions()
        .setMaxEventLoopExecuteTime(500)
        .setMaxEventLoopExecuteTimeUnit(TimeUnit.MILLISECONDS)
        .setBlockedThreadCheckInterval(1)
        .setBlockedThreadCheckIntervalUnit(TimeUnit.SECONDS)
        .setEventLoopPoolSize(4)
    );
    vertx.deployVerticle(EventLoopMain.class.getName(),
      new DeploymentOptions()
        .setInstances(4));
  }


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    logger.debug("Start {} verticle", getClass().getName());
    startPromise.complete();
    // Do not block inside a verticle
    //Thread.sleep(3000);
  }
}

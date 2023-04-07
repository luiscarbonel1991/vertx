package com.devlach.vertx_web;

import com.devlach.vertx_web.config.ConfigLoader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class VersionInfoVerticle extends AbstractVerticle {


  private static final Logger logger = LogManager.getLogger(VersionInfoVerticle.class);


  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    ConfigLoader.load(vertx)
      .onSuccess(config -> {
        logger.info("Current application version is: {}", config.getVersion());
        startPromise.complete();
      })
      .onFailure(err -> {
        logger.error("Config load failed", err);
        startPromise.fail(err);
      });
  }
}

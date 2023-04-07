package com.devlach.vertx_web.assets;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class GetAssetsHandler implements Handler<RoutingContext> {

  private static final Logger logger = LogManager.getLogger(GetAssetsHandler.class);
  public static final List<String> ASSETS = List.of("AAPL", "MSFT", "AMZN", "GOOG", "FB");
  @Override
  public void handle(RoutingContext routingContext) {
    final JsonArray assets = new JsonArray();
    ASSETS.stream().map(Asset::new).forEach(assets::add);
    logger.info("Path {} assets: {}", routingContext.normalizedPath(), assets.encode());
    // Delay to simulate a slow response
    routingContext.vertx().setTimer(5000, id -> {
      routingContext
        .response()
        .setStatusCode(200)
        // Set the content type to JSON
        .putHeader("content-type", "application/json")
        .end(assets.encodePrettily());
    });


  }
}

package com.devlach.vertx_web.assets;

import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class AssetsRestApi {

  private static final Logger logger = LogManager.getLogger(AssetsRestApi.class);

  public static final List<String> ASSETS = List.of("AAPL", "MSFT", "AMZN", "GOOG", "FB");

  public static void route(Router router) {
    router.get("/assets").handler(new GetAssetsHandler());
  }
}

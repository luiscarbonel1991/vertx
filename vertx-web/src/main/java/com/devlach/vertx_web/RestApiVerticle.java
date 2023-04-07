package com.devlach.vertx_web;

import com.devlach.vertx_web.assets.AssetsRestApi;
import com.devlach.vertx_web.config.ConfigLoader;
import com.devlach.vertx_web.quotes.QuoteRestApi;
import com.devlach.vertx_web.watchlist.WatchListRestApi;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestApiVerticle extends AbstractVerticle {

  private static final Logger logger = LogManager.getLogger(RestApiVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onSuccess(config -> {
        logger.info("Config loaded: {}", config);
        startHttpServer(startPromise, config.getServerPort());
      })
      .onFailure(err -> {
        logger.error("Config load failed", err);
        startPromise.fail(err);
      });
  }

  private void startHttpServer(Promise<Void> startPromise, int serverPort) {
    final Router router = Router.router(vertx);
    router.route()
      .handler(BodyHandler.create())
      .failureHandler(errorFailure());
    AssetsRestApi.route(router);
    QuoteRestApi.route(router);
    WatchListRestApi.route(router);

    vertx.createHttpServer()
      .requestHandler(router)
      .exceptionHandler(err -> logger.error("Http Server error: ", err))
      .listen(serverPort, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          logger.info("HTTP server started on port {}", serverPort);
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private static Handler<RoutingContext> errorFailure() {
    return err -> {
      if (err.response().ended()) {
        return;
      }

      logger.error("Route error: ", err.failure());
      if (err.statusCode() == 404) {
        err.response().end(new JsonObject().put("error", "Not found").encodePrettily());
        return;
      }

      err.response().end(new JsonObject().put("error", "Internal server error").encodePrettily());

    };
  }
}

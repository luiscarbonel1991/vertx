package com.devlach.vertx_web.watchlist;

import com.devlach.vertx_web.assets.Asset;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WatchListRestApi {


  private static final Logger logger = LogManager.getLogger(WatchListRestApi.class);


  private static final String WATCHLIST_PATH = "/account/watchlist/:accountId";

  public static void route(Router router) {
    final Map<UUID, List<Asset>> watchList = new HashMap<>();
    router.get(WATCHLIST_PATH).handler(req -> {
      var accountId = req.pathParam("accountId");
      logger.debug("Path {} accountId: {}", req.normalizedPath(), accountId);
      final List<Asset> assets = watchList.get(UUID.fromString(accountId));
      if (assets == null) {
        req.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
        return;
      }
      req.response().end(new JsonArray(assets).toBuffer());
    });

    router.put(WATCHLIST_PATH).handler(req -> {
      var accountId = req.pathParam("accountId");
      logger.debug("Path {} accountId: {}", req.normalizedPath(), accountId);
      var watchListToUpdateJson = req.body().asJsonObject();
      watchList.put(UUID.fromString(accountId), watchListToUpdateJson.mapTo(WatchList.class).getAssets());
      req.response().end(watchListToUpdateJson.toBuffer());
    });

    router.delete(WATCHLIST_PATH).handler(req -> {
      var accountId = req.pathParam("accountId");
      logger.debug("Path {} accountId: {}", req.normalizedPath(), accountId);
      if(!watchList.containsKey(UUID.fromString(accountId))){
        req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
        return;
      }
      watchList.remove(UUID.fromString(accountId));
      req.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end();
    });
  }
}

package com.devlach.vertx_web;

import com.devlach.vertx_web.assets.Asset;
import com.devlach.vertx_web.watchlist.WatchList;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@ExtendWith(VertxExtension.class)
public class TestMainVerticle {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void returs_all_assets(Vertx vertx, VertxTestContext testContext) {
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
    client.get("/assets")
      .send()
      .onComplete(testContext.succeeding(response -> {
        response.bodyAsJsonArray().forEach(asset -> {
          testContext.verify(() -> {
            assert asset instanceof JsonObject;
            assert ((JsonObject) asset).containsKey("name");
          });
          testContext.completeNow();
        });
      }))
      .onFailure(testContext::failNow);

  }

  @Test
  void givenUnknownAsset_whenGetQuote_then404(Vertx vertx, VertxTestContext testContext) {
    // given
    String unknownAsset = "UNKNOWN";
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));

    // when
    client.get("/quotes/" + unknownAsset)
      .send()
      .onComplete(testContext.succeeding(resp -> {
        // then
        testContext.verify(() -> {
          assert resp.statusCode() == HttpResponseStatus.NOT_FOUND.code();
        });
        testContext.completeNow();
      }))
      .onFailure(testContext::failNow);
  }

  @Test
  void givenWatchList_whenPut_thenReturnsWatchList(Vertx vertx, VertxTestContext testContext) {
    // given
    String accountId = UUID.randomUUID().toString();
    String path = "/account/watchlist/" + accountId;
    WebClient client = WebClient.create(vertx, new WebClientOptions().setDefaultPort(8888));
    WatchList watchListToUpdate = new WatchList(
      Arrays.asList(
        new Asset("AAPL"),
        new Asset("MSFT")
      )
    );

    // when
    client.put(path)
      .sendJsonObject(
        JsonObject.mapFrom(watchListToUpdate)
      )
      .onComplete(testContext.succeeding(resp -> {
        // then
        testContext.verify(() -> {
          assert resp.statusCode() == HttpResponseStatus.OK.code();
          assert Objects.equals(resp.bodyAsJsonObject().encode(), JsonObject.mapFrom(watchListToUpdate).encode());
        });
        testContext.completeNow();
      }))
      .compose(resp -> client.get(path)
        .send()
        .onComplete(testContext.succeeding(respCompose -> {
          // then
          testContext.verify(() -> {
            assert respCompose.statusCode() == HttpResponseStatus.OK.code();
            assert Objects.equals(respCompose.bodyAsJsonObject().encode(), JsonObject.mapFrom(watchListToUpdate).encode());
          });
          testContext.completeNow();
        })))
      .onFailure(testContext::failNow);
  }
}

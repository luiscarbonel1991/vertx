package com.devlach.vertx_web.quotes;

import com.devlach.vertx_web.assets.Asset;
import com.devlach.vertx_web.assets.AssetsRestApi;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class QuoteRestApi {



  public static void route(Router router) {

    router.get("/quotes/:asset").handler(req -> {
     final String asset = req.pathParam("asset");
      final Optional<Quote> quote = getQuote(asset);
      if (quote.isEmpty()){
        // Change this code for custom exception. For example: NotFoundException
        req.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code()).end();
        return;
      }

      final JsonObject json = JsonObject.mapFrom(quote.get());
      req.response().end(json.encodePrettily());
    });
  }

  private static BigDecimal randomBigDecimal() {
    return BigDecimal.valueOf(Math.random() * 100);
  }

  private static Optional<Quote> getQuote(String asset){
    return Optional.ofNullable(cacheQuotes().get(asset));
  }

  private static Map<String, Quote> cacheQuotes(){
    Map<String, Quote> cacheQuotes = new HashMap<>();
    AssetsRestApi.ASSETS.stream().map(Asset::new).forEach(asset -> {
      Quote quote = Quote.builder()
        .asset(asset)
        .ask(randomBigDecimal())
        .bid(randomBigDecimal())
        .lastPrice(randomBigDecimal())
        .volume(randomBigDecimal())
        .build();
      cacheQuotes.put(asset.getName(), quote);
    });
    return cacheQuotes;
  }
}

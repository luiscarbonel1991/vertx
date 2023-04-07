package com.devlach.vertx_web.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.io.ObjectInputFilter;

public class ConfigLoader {

  public static final String SERVER_PORT = "SERVER_PORT";
  public static Future<BrokerConfig> load(Vertx vertx) {
    var envStore = new ConfigStoreOptions()
      .setType("env")
      .setConfig(new JsonObject()
        .put("keys", new JsonArray()
          .add(SERVER_PORT)
        )
      );

    var fileStore = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject()
        .put("path", "config.yml")
      );

    var retriever = ConfigRetriever.create(vertx,
      new io.vertx.config.ConfigRetrieverOptions()
        .addStore(fileStore)
        .addStore(envStore)
    );


    return retriever.getConfig()
      .map(BrokerConfig::from);
  }
}

package com.devlach.vertx_web.config;

import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import static com.devlach.vertx_web.config.ConfigLoader.SERVER_PORT;

@Builder
@Value
@ToString
public class BrokerConfig {


  int serverPort;

  String version;

  public static BrokerConfig from(final JsonObject config) {

    var version = config.getString("version");
    if (version == null) {
      throw new IllegalArgumentException("version is required");
    }
   return BrokerConfig.builder()
     .serverPort(config.getInteger(SERVER_PORT, 8080))
      .version(version)
     .build();
  }
}

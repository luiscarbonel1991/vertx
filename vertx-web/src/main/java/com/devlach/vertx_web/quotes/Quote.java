package com.devlach.vertx_web.quotes;

import com.devlach.vertx_web.assets.Asset;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Quote {
  Asset asset;
  BigDecimal volume;
  BigDecimal bid;
  BigDecimal ask;
  BigDecimal lastPrice;
}

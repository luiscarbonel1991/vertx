package com.devlach.vertx_web.watchlist;

import com.devlach.vertx_web.assets.Asset;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WatchList {
  List<Asset> assets;
}

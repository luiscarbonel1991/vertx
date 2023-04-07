package com.devlach.vertx_starter;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class TestFuturePromise {

  private static final Logger logger = LogManager.getLogger(TestFuturePromise.class);


  @Test
  void givenPromise_whenComplete_thenSuccess(Vertx vertx, VertxTestContext context) {
    vertx.executeBlocking(
      promise -> {
        logger.debug("Executing blocking code");
        promise.complete("Hello, I'm here!!");
      },
      replay -> {
        logger.debug("Executing replay code");
        context.verify(() -> {
          logger.debug("Verifying replay code");
          context.completeNow();
        });
      }
    );

  }

  @Test
  void givenPromise_whenFail_thenFail(Vertx vertx, VertxTestContext context) {
    vertx.executeBlocking(
      promise -> {
        logger.debug("Executing blocking code");
        promise.fail("Hello, I'm here with a error!!");
      },
      replay -> {
        logger.debug("Executing replay code");
        context.verify(() -> {
          logger.debug("Verifying replay code");
          context.completeNow();
        });
      }
    );

  }

  @Test
  void givenPromise_whenSuccess_thenFutureSuccess(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();

    vertx.setPeriodic(1000, id -> {
      logger.debug("Executing blocking code");
      promise.complete("Hello, I'm here!!");
    });

    promise.future().onSuccess(replay -> {
        logger.debug("Executing replay code");
        context.verify(() -> {
          logger.debug("Verifying replay code");
          context.completeNow();
        });
      })
      .onFailure(context::failNow);
  }

  @Test
  void givenPromise_whenFail_thenFutureFail(Vertx vertx, VertxTestContext context) {
    final Promise<String> promise = Promise.promise();

    vertx.setPeriodic(1000, id -> {
      logger.debug("Executing blocking code");
      promise.fail("Hello, I'm here!!");
    });

    promise.future().onSuccess(replay -> {
        logger.debug("Executing replay code");
        context.verify(() -> {
          logger.debug("Verifying replay code");
          context.completeNow();
        });
      })
      .onFailure(error -> {
        logger.debug("Executing error code");
        context.verify(() -> {
          logger.debug("Verifying error code");
          context.completeNow();
        });
      });
  }

  @Test
  void givenHttpServer_whenCoordinatesFutures_thenFuturesSuccess(Vertx vertx, VertxTestContext context) {
    vertx.createHttpServer()
      .requestHandler(request -> {
        request.response().end("Hello, I'm here!!");
      })
      .listen(8080)
      .compose(server -> {
        logger.debug("First compose");
        return Future.succeededFuture(server);
      })
      .compose(server -> {
        logger.debug("Second compose");
        return Future.succeededFuture(server);
      })
      .onSuccess(server -> {
        logger.debug("Server started on port: " + server.actualPort());
        context.completeNow();
      })
      .onFailure(context::failNow);
  }

  @Test
  void givenPromises_whenCompositionFutures_thenAllFuturesSuccess(Vertx vertx, VertxTestContext context){
    // given
    var promise1 = Promise.<Void>promise();
    var promise2 = Promise.<Void>promise();
    var promise3 = Promise.<Void>promise();

    // when
    var future1 = promise1.future();
    var future2 = promise2.future();
    var future3 = promise3.future();

    // then
    CompositeFuture.all(future1, future2, future3)
      .onSuccess(replay -> {
        logger.debug("All futures success");
        context.completeNow();
      })
      .onFailure(context::failNow);

    // when
    promise1.complete();
    promise2.complete();
    promise3.complete();
  }
}

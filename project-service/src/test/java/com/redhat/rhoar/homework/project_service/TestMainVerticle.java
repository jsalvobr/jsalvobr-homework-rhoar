package com.redhat.rhoar.homework.project_service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.vertx.core.Vertx;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;

@RunWith(VertxUnitRunner.class)
public class TestMainVerticle {

  @Before
  void deploy_verticle(Vertx vertx, TestContext testContext) {
//    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void verticle_deployed(Vertx vertx, TestContext testContext) throws Throwable {
//    testContext.completeNow();
  }
}

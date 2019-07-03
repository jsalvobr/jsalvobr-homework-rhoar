package com.redhat.rhoar.homework.project.service;

import java.util.List;

import com.redhat.rhoar.homework.project.model.Project;

import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

@ProxyGen
public interface ProjectService {
	
	final static String ADDRESS = "project-service";
	
    static ProjectService create(Vertx vertx, JsonObject config, MongoClient client) {
        return new ProjectServiceImpl(vertx, config, client);
    }

    static ProjectService createProxy(Vertx vertx) {
        return new ProjectServiceVertxEBProxy(vertx, ADDRESS);
    }
	
	void getProjects(Handler<AsyncResult<List<Project>>> resulthandler);
	void getProject(String projectId, Handler<AsyncResult<Project>> resulthandler);
	void getProjectsWithStatus(String theStatus,Handler<AsyncResult<List<Project>>> resulthandler);

}

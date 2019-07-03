package com.redhat.rhoar.homework.project.api;

import java.util.List;

import com.redhat.rhoar.homework.project.model.Project;
import com.redhat.rhoar.homework.project.service.ProjectService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class ApiVerticle extends AbstractVerticle {
	
	private ProjectService projectService;

	public ApiVerticle(ProjectService projectService) {
		this.projectService = projectService;
	}

	
	@Override
    public void start(Future<Void> startFuture) throws Exception {

        Router router = Router.router(vertx);
        router.get("/projects").handler(this::getProjects);
        router.get("/projects/:projectId").handler(this::getProject);
        router.route("/projects").handler(BodyHandler.create());
        router.get("/projects/status/:theStatus").handler(this::getProjectsWithStatus);

        //Health Checks
        router.get("/health/readiness").handler(rc -> rc.response().end("OK"));

        vertx.createHttpServer()
            .requestHandler(router::accept)
            .listen(config().getInteger("project.http.port", 8080), result -> {
                if (result.succeeded()) {
                    startFuture.complete();
                } else {
                    startFuture.fail(result.cause());
                }
            });
    }
	
	private void getProjects(RoutingContext rc) {
        projectService.getProjects(ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result();
                JsonArray json = new JsonArray();
                projects.stream()
                    .map(p -> p.toJson())
                    .forEach(p -> json.add(p));
                rc.response()
                    .putHeader("Content-type", "application/json")
                    .end(json.encodePrettily());
            } else {
                rc.fail(ar.cause());
            }
        });
    }
	
	private void getProject(RoutingContext rc) {
        String projectId = rc.request().getParam("itemid");
        projectService.getProject(projectId, ar -> {
            if (ar.succeeded()) {
                Project project = ar.result();
                JsonObject json;
                if (project != null) {
                    json = project.toJson();
                    rc.response()
                        .putHeader("Content-type", "application/json")
                        .end(json.encodePrettily());
                } else {
                    rc.fail(404);
                }
            } else {
                rc.fail(ar.cause());
            }
        });
    }
	
	
	private void getProjectsWithStatus(RoutingContext rc) {
		String theStatus = rc.request().getParam("theStatus");
        projectService.getProjectsWithStatus(theStatus,ar -> {
            if (ar.succeeded()) {
                List<Project> projects = ar.result();
                JsonArray json = new JsonArray();
                projects.stream()
                    .map(p -> p.toJson())
                    .forEach(p -> json.add(p));
                rc.response()
                    .putHeader("Content-type", "application/json")
                    .end(json.encodePrettily());
            } else {
                rc.fail(ar.cause());
            }
        });
    }
	
}

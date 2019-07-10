package com.redhat.rhoar.homework.api.gateway;


import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;


@Path("/")
public class ProjectGateway {
	
	Client client = ClientBuilder.newClient();
	
	@Inject
    @ConfigurationValue("project.service.url")
    private String projectUrl;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects")
	public Response getProjects() {
		return Response.ok("Hello from Thorntail 1!").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects/{projectId}")
	public Response getProject(@PathParam("projectId") String projectId) {
		return Response.ok("Hello from Thorntail 2!").build();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/projects/status/{theStatus}")
	public Response getProjectsWithStatus(@PathParam("theStatus") String theStatus) {
		return Response.ok("Hello from Thorntail 3!").build();
	}
}

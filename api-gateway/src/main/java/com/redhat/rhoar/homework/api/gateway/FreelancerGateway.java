package com.redhat.rhoar.homework.api.gateway;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;


@Path("/")
public class FreelancerGateway {
	
	Client client = ClientBuilder.newClient();
	
	@Inject
    @ConfigurationValue("freelancer.service.url")
    private String freelancerUrl;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers")
	public Response getFreelancers() {
//		return Response.ok("Hello from Thorntail!").build();
		if (freelancerUrl == null) {
			freelancerUrl = "https://petstore.swagger.io/v2/store/inventory";
		}
		return client
			      .target(freelancerUrl)
			      .request(MediaType.APPLICATION_JSON)
			      .get();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers/{freelancerId}")
	public Response getFreelancer(@PathParam("freelancerId") String freelancerId ) {
		
		return Response.ok("Hello from Thorntail!").build();
	}
}

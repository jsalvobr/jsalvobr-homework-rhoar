package com.redhat.rhoar.homework.api.gateway;


import javax.enterprise.context.ApplicationScoped;
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

@ApplicationScoped
@Path("/gateway")
public class FreelancerGateway {
	
	Client client = ClientBuilder.newClient();
	
	@Inject
    @ConfigurationValue("freelancer.service.url")
    private String freelancerUrl;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers")
	public Response getFreelancers() {
		return client
			      .target(freelancerUrl + "/freelancers")
			      .request(MediaType.APPLICATION_JSON)
			      .get();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/freelancers/{freelancerId}")
	public Response getFreelancer(@PathParam("freelancerId") String freelancerId ) {
		return client
			      .target(freelancerUrl + "/freelancers/" + freelancerId)
			      .request(MediaType.APPLICATION_JSON)
			      .get();
	}
}

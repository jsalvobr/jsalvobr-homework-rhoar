package com.redhat.rhoar.homework.freelancer.rest;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.redhat.rhoar.homework.freelancer.model.Freelancer;
import com.redhat.rhoar.homework.freelancer.service.FreelancerService;

@Path("/")
@Component
public class FreelancerEndpoint {

	@Autowired
    private FreelancerService freelancerService;

	@GET
    @Path("/freelancers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Freelancer> getFreelancers() {
		return freelancerService.getFreelancers();
	}


    @GET
    @Path("/freelancers/{freelancerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Freelancer getFreelancer(@PathParam("freelancerId") String freelancerId) {
		return freelancerService.getFreelancer(freelancerId);
	}

    @PostConstruct
	public void cargaData() {
		Freelancer freelancer = new Freelancer();
		freelancer.setFirstName("Juan");
		freelancer.setLastName("Prueba");
		freelancer.setEmailAddress("correo@prueba.cl");
		String[] skills = new String[2];
		skills[0] = "java";
		skills[1] = "sql";
		freelancer.setSkills(skills);
		System.out.println("El Usuario 1 a insertar es -------> " + freelancer);
		freelancerService.addFreelancer(freelancer);
		
		Freelancer freelancer2 = new Freelancer();
		freelancer2.setFirstName("Pedro");
		freelancer2.setLastName("Salvo");
		freelancer2.setEmailAddress("correo2@prueba.cl");
		String[] skills2 = new String[2];
		skills2[0] = "openshift";
		skills2[1] = "ansible";
		freelancer2.setSkills(skills2);
		System.out.println("El Usuario 2 a insertar es -------> " + freelancer2);
		freelancerService.addFreelancer(freelancer2);
    }
    
}

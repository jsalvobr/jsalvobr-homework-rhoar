package com.redhat.rhoar.homework.api.gateway;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.redhat.rhoar.homework.api.gateway.model.Freelancer;


@RunWith(Arquillian.class)
public class FreelancerGatewayTest{
	
	private static String port = "8080";
	
	private Client client;
	
	
    @Before
    public void before() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }

    @Rule
    public WireMockRule freelancerServiceMock = new WireMockRule(11111);

    
    @Deployment
	public static Archive<?> createDeployment() {
    	WebArchive archive = ShrinkWrap.create(WebArchive.class);
    	archive.addAsResource("project-test.yml", "project-defaults.yml");
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();
		archive.addAsLibraries(files);
		archive.addPackages(true,FreelancerGateway.class.getPackage());
		archive.addPackages(true,Freelancer.class.getPackage());		
		return archive;
	}
    
    
    @Test
    public void getFreelancers() throws Exception {
    	stubFor(get(urlEqualTo("/freelancers")).willReturn(aResponse()
                .withStatus(200).withHeader("Content-Type", "application/json")
              .withBody(buildFreelancerResponse())));

        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/freelancers");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("freelancerId").asText(), equalTo("123456"));
        assertThat(node.get("firstName").asText(), equalTo("Juan"));
        assertThat(node.get("lastName").asText(), equalTo("Prueba"));
        assertThat(node.get("emailAddress").asText(), equalTo("correo@prueba.cl"));
        assertThat(node.get("skills").get(0).asText(), equalTo("java"));
        assertThat(node.get("skills").get(1).asText(), equalTo("sql"));
        freelancerServiceMock.verify(getRequestedFor(urlEqualTo("/freelancers")));
    }
    
    @Test
    public void getFreelancer() throws Exception {
    	stubFor(get(urlEqualTo("/freelancers/123456")).willReturn(aResponse()
                .withStatus(200).withHeader("Content-Type", "application/json")
              .withBody(buildFreelancerResponse())));

        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/freelancers").path("/123456");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("freelancerId").asText(), equalTo("123456"));
        assertThat(node.get("firstName").asText(), equalTo("Juan"));
        assertThat(node.get("lastName").asText(), equalTo("Prueba"));
        assertThat(node.get("emailAddress").asText(), equalTo("correo@prueba.cl"));
        assertThat(node.get("skills").get(0).asText(), equalTo("java"));
        assertThat(node.get("skills").get(1).asText(), equalTo("sql"));
        freelancerServiceMock.verify(getRequestedFor(urlEqualTo("/freelancers/123456")));
    }




    private String buildFreelancerResponse() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
       
        Freelancer freelancer = new Freelancer();
        freelancer.setFreelancerId("123456");
		freelancer.setFirstName("Juan");
		freelancer.setLastName("Prueba");
		freelancer.setEmailAddress("correo@prueba.cl");
		String[] skills = new String[2];
		skills[0] = "java";
		skills[1] = "sql";
		freelancer.setSkills(skills);
        return mapper.writeValueAsString(freelancer);
    }

}

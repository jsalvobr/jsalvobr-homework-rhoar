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
import com.redhat.rhoar.homework.api.gateway.model.Project;
import com.redhat.rhoar.homework.api.gateway.model.Status;


@RunWith(Arquillian.class)
public class ProjectGatewayTest{
	
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
    public WireMockRule projectServiceMock = new WireMockRule(11111);

    
    @Deployment
	public static Archive<?> createDeployment() {
    	WebArchive archive = ShrinkWrap.create(WebArchive.class);
    	archive.addAsResource("project-test.yml", "project-defaults.yml");
		File[] files = Maven.resolver().loadPomFromFile("pom.xml")
				.importRuntimeAndTestDependencies().resolve()
				.withTransitivity().asFile();
		archive.addAsLibraries(files);
		archive.addPackages(true,ProjectGateway.class.getPackage());
		archive.addPackages(true,Project.class.getPackage());		
		return archive;
	}
    
    
    @Test
    public void getProjects() throws Exception {
    	stubFor(get(urlEqualTo("/projects")).willReturn(aResponse()
                .withStatus(200).withHeader("Content-Type", "application/json")
              .withBody(buildProjectResponse())));

        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("projectId").asText(), equalTo("p01"));
        assertThat(node.get("ownerFirstName").asText(), equalTo("Pedro"));
        assertThat(node.get("ownerLastName").asText(), equalTo("Prueba"));
        assertThat(node.get("ownerEmailAddress").asText(), equalTo("project@prueba.cl"));
        assertThat(node.get("projectTitle").asText(), equalTo("Openshift"));
        assertThat(node.get("projectDescription").asText(), equalTo("orientado a RHOAR"));
        assertThat(node.get("status").asText(), equalTo("open"));
        projectServiceMock.verify(getRequestedFor(urlEqualTo("/projects")));
    }
    
    @Test
    public void getProject() throws Exception {
    	stubFor(get(urlEqualTo("/projects/p01")).willReturn(aResponse()
                .withStatus(200).withHeader("Content-Type", "application/json")
              .withBody(buildProjectResponse())));

        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects").path("/p01");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("projectId").asText(), equalTo("p01"));
        assertThat(node.get("ownerFirstName").asText(), equalTo("Pedro"));
        assertThat(node.get("ownerLastName").asText(), equalTo("Prueba"));
        assertThat(node.get("ownerEmailAddress").asText(), equalTo("project@prueba.cl"));
        assertThat(node.get("projectTitle").asText(), equalTo("Openshift"));
        assertThat(node.get("projectDescription").asText(), equalTo("orientado a RHOAR"));
        assertThat(node.get("status").asText(), equalTo("open"));
        projectServiceMock.verify(getRequestedFor(urlEqualTo("/projects/p01")));
    }
    
    @Test
    public void getProjectsForStatus() throws Exception {
    	stubFor(get(urlEqualTo("/projects/status/open")).willReturn(aResponse()
                .withStatus(200).withHeader("Content-Type", "application/json")
              .withBody(buildProjectResponse())));

        WebTarget target = client.target("http://localhost:" + port).path("/gateway").path("/projects").path("/status").path("/open");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));

        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("projectId").asText(), equalTo("p01"));
        assertThat(node.get("ownerFirstName").asText(), equalTo("Pedro"));
        assertThat(node.get("ownerLastName").asText(), equalTo("Prueba"));
        assertThat(node.get("ownerEmailAddress").asText(), equalTo("project@prueba.cl"));
        assertThat(node.get("projectTitle").asText(), equalTo("Openshift"));
        assertThat(node.get("projectDescription").asText(), equalTo("orientado a RHOAR"));
        assertThat(node.get("status").asText(), equalTo("open"));
        projectServiceMock.verify(getRequestedFor(urlEqualTo("/projects/status/open")));
    }




    private String buildProjectResponse() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        
        Project project = new Project();
        project.setProjectId("p01");
        project.setOwnerFirstName("Pedro");
        project.setOwnerLastName("Prueba");
        project.setOwnerEmailAddress("project@prueba.cl");
        project.setProjectTitle("Openshift");
        project.setProjectDescription("orientado a RHOAR");
        project.setStatus(Status.open);      
       
        return mapper.writeValueAsString(project);
    }

}

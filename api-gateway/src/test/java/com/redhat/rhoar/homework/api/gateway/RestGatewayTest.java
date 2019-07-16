package com.redhat.rhoar.homework.api.gateway;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.MatcherAssert.assertThat;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(Arquillian.class)
public class RestGatewayTest {
	
	private static String port = "8080";
    
    private Client client;

    
    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, RestApplication.class.getPackage());
    }

    @Before
    public void before() throws Exception {
        client = ClientBuilder.newClient();
    }

    @After
    public void after() throws Exception {
        client.close();
    }
    
//    @Test
//    @RunAsClient
//    public void testGetInventory() throws Exception {
//        WebTarget target = client.target("http://localhost:" + port).path("/inventory").path("/123456");
//        Response response = target.request(MediaType.APPLICATION_JSON).get();
//        assertThat(response.getStatus(), equalTo(new Integer(200)));
//        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
//        assertThat(value.getString("itemId", null), equalTo("123456"));
//        assertThat(value.getString("location", null), equalTo("location"));
//        assertThat(value.getInt("quantity", 0), equalTo(new Integer(99)));
//        assertThat(value.getString("link", null), equalTo("link"));
//    }

//    @Test
//    @RunAsClient
//    public void testGetInventoryWithStoreStatus() throws Exception {
//        WebTarget target = client.target("http://localhost:" + port).path("/inventory").path("/123456").queryParam("storeStatus", true);
//        Response response = target.request(MediaType.APPLICATION_JSON).get();
//        assertThat(response.getStatus(), equalTo(new Integer(200)));
//        JsonObject value = Json.parse(response.readEntity(String.class)).asObject();
//        assertThat(value.getString("itemId", null), equalTo("123456"));
//        assertThat(value.getString("location", null), equalTo("location [MOCK]"));
//        assertThat(value.getInt("quantity", 0), equalTo(new Integer(99)));
//        assertThat(value.getString("link", null), equalTo("link"));
//    }

    

    @Test
    public void healtCheckEndpoint() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/health");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), equalTo(HttpStatus.SC_OK));
        JsonNode node = new ObjectMapper(new JsonFactory()).readTree(response.readEntity(String.class));
        assertThat(node.get("outcome").asText(), equalTo("UP"));
    }

    @Test
    public void infoEndpointIsNotEnabled() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/info");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus() , equalTo(new Integer(404)));
    }

    @Test
    public void pauseEndpointIsNotEnabled() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/pause");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), anyOf(equalTo(new Integer(404)), equalTo(new Integer(401))));
    }

    @Test
    public void restartEndpointIsNotEnabled() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/restart");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), anyOf(equalTo(new Integer(404)), equalTo(new Integer(401))));
    }

    @Test
    public void resumeEndpointIsNotEnabled() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/resume");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), anyOf(equalTo(new Integer(404)), equalTo(new Integer(401))));
    }

    @Test
    public void refreshEndpointIsNotEnabled() throws Exception {
    	WebTarget target = client.target("http://localhost:" + port).path("/refresh");
    	Response response = target.request(MediaType.APPLICATION_JSON).get();
        assertThat(response.getStatus(), anyOf(equalTo(new Integer(404)), equalTo(new Integer(401))));
    }
}

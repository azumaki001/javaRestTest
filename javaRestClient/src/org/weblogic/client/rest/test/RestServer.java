package org.weblogic.client.rest.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class RestServer {

	public void TestRestServer(){

		String uri = "http://localhost:8080/JerseyServer/rest/UserInfoService/hellojson";

		Client client = Client.create();

		WebResource webResource = client.resource(uri);

		webResource.header("Content-Type", "application/json;charset=UTF-8");

		String input = "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

		ClientResponse response = webResource
				.accept("application/json")
				.type("application/json")
				.post(ClientResponse.class, input);

//		if (response.getStatus() != 201||response.getStatus() != 200) {
//			throw new RuntimeException("Failed : HTTP error code : "
//					+ response.getStatus());
//		}

		//RespEntity resp = response.getEntity(RespEntity.class);

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);
	}

}

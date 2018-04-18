package com.server.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.server.rest.entity.HelloResult;

/**
 *
 * @author pavithra
 *
 */

// http://localhost:8080/JerseyServer/rest/UserInfoService/name/2

@Path("UserInfoService")
public class UserInfo {
	@GET
	@Path("/name/{i}")
	@Produces(MediaType.TEXT_XML)
	public String userName(@PathParam("i") String i) {
		String name = i;
		return "<User>" + "<Name>" + name + "</Name>" + "</User>";
	}

	// http://localhost:8080/JerseyServer/rest/UserInfoService/post

	@POST
	@Path("/post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createTrackInJSON(String str) {

		String result = "Track saved : " + str;
		return Response.status(201).entity(result).build();

	}

	@POST
	@Path("/hellojson")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HelloResult helloJson(String str) {

		HelloResult result = new HelloResult();
//		result.mebr_no="99900213";
//		result.cust_id="7765234";
//		result.ufpr_mebr_no="001";
//		result.able_i_nam="rara";
//		result.flg="1";
		return result;

	}
	// @POST
	// @Path("/post")
	// @Consumes(MediaType.APPLICATION_JSON)
	// @Produces(MediaType.APPLICATION_JSON)
	// public String createProductInJSON(String request) throws JSONException {
	// // RestRequestServer restRequestServer = new RestRequestServer();
	// //
	// // String json = request;
	// //
	// // JSONObject obj = JSONObject.fromObject(json);
	// // restRequestServer = (RestRequestServer)JSONObject.toBean(obj,
	// // RestRequestServer.class);
	// //
	// // String result = "Product created : " + restRequestServer;
	// // return Response.status(201).entity(result).build();
	//
	// JSONObject postData = new JSONObject();
	// postData.put("name", "1111");
	//
	// return postData.toString();
	//
	// }
}

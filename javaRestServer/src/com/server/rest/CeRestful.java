package com.server.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import com.server.rest.entity.HelloResult;


@Path("MindsCustomerInfoReception/")
public class CeRestful {

	@GET
	@Path("/MindsCustomerInfoReception/api/v1")
	@Produces(MediaType.TEXT_PLAIN)
	public String testJson(String str) {

		return "{\"title\":\"頭\",\"boday\":\"こんにちゃ\"}";

	}

	@POST
	@Path("/MindsCustomerInfoReception/api/v2")
	@Produces(MediaType.TEXT_HTML)
	public String testJson2(String str) {

		return "{\"title\":\"頭2\",\"boday\":\"こんにちゃ2\"}";

	}

	@POST
	@Path("/MindsCustomerInfoReception/api/v3")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public HelloResult testJson(@Context HttpHeaders headers) {

		List<String> h = headers.getRequestHeader("x-ces-jwt");
		MultivaluedMap<String, String> map = headers.getRequestHeaders();

		List<String> a = map.get("x-ces-jwt");

		HelloResult result = new HelloResult();
		result.mebr_no="99900213";
		result.cust_id="7765234";
		result.ufpr_mebr_no="001";
		result.able_i_nam="able_i_nam000";
		result.flg="1";
		return result;

	}
}

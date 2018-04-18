package org.weblogic.client.rest.test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import jp.co.ana.ces.auth.JWTAuth;

import org.weblogic.client.rest.test.entity.RespEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class MyJerseyClientRestful {

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {

	// CE基盤より提供されるClientId
	String clientId = "...";
	// CE基盤より提供される秘密鍵ファイルのパス
	String secretKeyFilePath = "...";

	// JWT作成
	String jwt = JWTAuth.generateJWT(clientId, secretKeyFilePath);

	System.out.println("★JWT★"+jwt);


	ClientConfig cc = new DefaultClientConfig();
	// true
	cc.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING,
		Boolean.TRUE);
	Client client = Client.create(cc);

	// uri設定
	String uri = "http://localhost:8080/javaRestTest/rest/MindsCustomerInfoReception/MindsCustomerInfoReception/api/v3";

	WebResource webResource = client.resource(uri);

	//webResource.header("Content-Type", "application/json;charset=UTF-8");
	//webResource.header(JWTAuth.HTTP_HEADER_NAME, jwt);

	String input = "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

	ObjectMapper mapper = new ObjectMapper();


        ObjectMapper mapper1 = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        RespEntity respEntity = new RespEntity();

            respEntity.able_i_nam="3";

            //2017/04/19  20:17:38.123
            DateFormat bf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");//

            Date date = new Date();
            String format = bf.format(date);

            respEntity.setMebr_no("123");

        String json = mapper.writeValueAsString(respEntity);

	ClientResponse response = webResource.accept("application/json")
		.type("application/json").header(JWTAuth.HTTP_HEADER_NAME, jwt).post(ClientResponse.class, json);

	// if (response.getStatus() != 201||response.getStatus() != 200) {
	// throw new RuntimeException("Failed : HTTP error code : "
	// + response.getStatus());
	// }

	// RespEntity resp = response.getEntity(RespEntity.class);

	System.out.println("Output from Server .... \n");
	String output = response.getEntity(String.class);

        RespEntity hoge = mapper.readValue(output, RespEntity.class);

	System.out.println(output);

    }

}

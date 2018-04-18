package org.weblogic.client.rest.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.json.impl.provider.entity.JSONRootElementProvider;

public class MyRest {

    public static void main(String[] args) {

	ClientConfig config = new DefaultClientConfig();
	config.getClasses().add(JSONRootElementProvider.class);
	Client client = Client.create(config);
    }

}

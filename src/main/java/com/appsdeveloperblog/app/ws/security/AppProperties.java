package com.appsdeveloperblog.app.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AppProperties {
	@Autowired
	private Environment env;
	
	public String getTokenSecret() {
		// read from application properties file
		return env.getProperty("tokenSecret");
	}
}

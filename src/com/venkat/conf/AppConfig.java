package com.venkat.conf;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("rest")
public class AppConfig extends Application {

	public AppConfig() {
		init();
	}

	private void init() {
		
	}

}
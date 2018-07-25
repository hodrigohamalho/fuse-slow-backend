package com.redhat.fuse.setup;

import org.apache.camel.component.hystrix.metrics.servlet.HystrixEventStreamServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class HystrixSetup {
	
	@Bean
	public HystrixEventStreamServlet hystrixServlet() {
		return new HystrixEventStreamServlet();
	}

	@Bean
	public ServletRegistrationBean servletRegistrationBean() {
		return new ServletRegistrationBean(new HystrixEventStreamServlet(), "/hystrix.stream");
	}
	
}

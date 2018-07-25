package com.redhat.fuse.setup;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class MessagingSetup {
	
	// @Value("${broker.host}")
	// private String brokerUrl;

	// @Value("${broker.username}")
	// private String userName;

	// @Value("${broker.password}")
	// private String password;

	@Bean
	public ActiveMQConnectionFactory coreConnectionFactory() throws Exception {
		final ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
	
		factory.setBrokerURL("vm://localhost");
		// factory.setUserName(userName.trim());
		// factory.setPassword(password.trim());

		factory.setTrustAllPackages(true);
		
		return factory;
	}    
	
}

package com.redhat.fuse.route;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.HystrixConfigurationDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.redhat.fuse.model.*;

@Component
public class MessagingRoute extends RouteBuilder {

	@Value("${smtp.host}")
	private String smtpHost;
	@Value("${smtp.to}")
	private String smtpTo;
	@Value("${smtp.from}")
	private String smtpFrom;
	@Value("${smtp.port}")
	private String smtpPort;
	@Value("${redelivery.delay}")
	private Integer relideveryDelay;
	@Value("${redelivery.maxAttempt}")
	private Integer maxAttempt;

	@Override
	public void configure() throws Exception {
		
		// Redelivery Policy
		errorHandler(defaultErrorHandler().redeliveryDelay(relideveryDelay).maximumRedeliveries(maxAttempt));
		
		HystrixConfigurationDefinition configuration = new HystrixConfigurationDefinition();
		configuration.circuitBreakerRequestVolumeThreshold(3);
		configuration.executionTimeoutInMilliseconds(1000);
		

		rest("/backend")
			.get("/").description("Backend mock service")
			.route()
			.setBody(constant("Backend system answer"));

		rest("/orders")
			.post("/").type(Order.class).description("Create a new Book")
				.route().routeId("insert-order").tracing()
				.wireTap("direct:send-to-backend")
				.setBody(constant("Your request was sent, thank you... Your confirmation will be sent by email."));
		
		from("direct:send-to-backend")
			.log("send-to-backend body: ${body}")
			.to(ExchangePattern.InOnly, "activemq:queue:backend");

		from("activemq:queue:backend")
			.log("Message received from the broker: ${body}")
			.process(new RestToSoapProcessor())
			.hystrix()
				.to("http://localhost:8080/api/backend") // This is a legacy and 'slow' system
			.onFallback()
				.log("entrei no fallback")
			.end()
			.log("Retorno: ${body}")
			.to(ExchangePattern.InOnly, "activemq:queue:send-email");

		from("activemq:queue:send-email")
			.log("enviando email")
			.removeHeaders("*")
			.to("string-template:email-template.tm")
			.to("mock:smtp");
			// .to("smtp:{{smtp.host}}:{{smtp.port}}?to={{smtp.to}}&from={{smtp.from}}");
		
	}
	
}
package com.redhat.fuse.route;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.Processor;
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
		

		rest("/backend")
			.get("/").description("Backend mock service")
			.route()
			.log("Request received on backend side")
			.setBody(constant("Backend system answer"));

		rest("/orders")
			.post("/").type(Order.class).description("Create a new Book")
				.route().routeId("insert-order").tracing()
				.wireTap("direct:create-order")
				.setBody(constant("Your request was sent, thank you... Your confirmation will be sent by email."));

		// mock a order creation
		from("direct:create-order")
			.log("Sending Order to AMQ broker: ${body}")
			.to(ExchangePattern.InOnly, "activemq:queue:backend");

		from("activemq:queue:backend")
			.transacted()
			.log("Message received from the AMQ broker: ${body}")
			.hystrix()
			.to("http4://localhost:8080/api/backend") // This is a legacy and 'slow' system
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
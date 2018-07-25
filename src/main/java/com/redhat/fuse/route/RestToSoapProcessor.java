package com.redhat.fuse.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestToSoapProcessor implements Processor{

	static final Logger logger = LoggerFactory.getLogger(RestToSoapProcessor.class.getName());

	@Override
	public void process(Exchange exchange) throws Exception {
		System.out.println("Processor....");
	}

}

// package com.redhat.fuse.route;

// import static org.hamcrest.CoreMatchers.*;
// import static org.junit.Assert.assertThat;

// import org.apache.camel.EndpointInject;
// import org.apache.camel.component.mock.MockEndpoint;
// import org.apache.camel.test.spring.CamelSpringBootRunner;
// import org.apache.camel.test.spring.MockEndpoints;
// import org.junit.Test;
// import org.junit.runner.RunWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
// import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import com.redhat.fuse.Application;

// @RunWith(CamelSpringBootRunner.class)
// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
// @MockEndpoints("activemq:queue:redhat")
// public class RestTest {
	
// 	   @Autowired
// 	   private TestRestTemplate restTemplate;
	   
// 	   @EndpointInject(uri = "activemq:queue:redhat")
// 	   private MockEndpoint mock;
	   
	   
	   
	   
// }